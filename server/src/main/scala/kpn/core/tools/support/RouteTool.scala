package kpn.core.tools.support

import kpn.api.custom.Subset
import kpn.core.database.Database
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.db.couch.Couch
import kpn.server.repository.BlackListRepositoryImpl
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.OrphanRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

object RouteTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-database", "analysis") { database =>
      new RouteTool(database).nonStandardRouteNames()
    }
  }
}

class RouteTool(database: Database) {

  def nonStandardRouteNames(): Unit = {
    val routeRepository = new RouteRepositoryImpl(database)
    val allRouteIds = routeRepository.allRouteIds()
    allRouteIds.zipWithIndex.foreach { case (routeId, index) =>
      if (((index + 1) % 100) == 0) {
        println(s"${index + 1}/${allRouteIds.size}")
      }
      routeRepository.routeWithId(routeId).foreach { routeInfo =>
        if (routeInfo.active) {
          if (!ignoreRouteName(routeInfo.summary.name)) {
            println(s"""== $routeId ${routeInfo.summary.name}""")
          }
        }
      }
    }
  }

  private def ignoreRouteName(name: String): Boolean = {
    if (name.contains(" - ")) {
      name.split(" - ") match {
        case Array(startNodeName, endNodeName) => ignoreRouteNodes(startNodeName, endNodeName)
        case _ => true
      }
    }
    else {
      name.split("-") match {
        case Array(startNodeName, endNodeName) => ignoreRouteNodes(startNodeName, endNodeName)
        case _ => true
      }
    }
  }

  private def ignoreRouteNodes(startNodeName: String, endNodeName: String): Boolean = {
    val avoid = Seq("?", "*", "o")
    if (avoid.contains(startNodeName) || avoid.contains(endNodeName)) {
      return true
    }
    if (!hasDigits(startNodeName) && !hasDigits(endNodeName)) {
      return true
    }
    if (allDigits(startNodeName) && allDigits(endNodeName)) {
      return true
    }

    if (allDigits(startNodeName) && !hasDigits(endNodeName)) {
      return false
    }
    if (allDigits(endNodeName) && !hasDigits(startNodeName)) {
      return false
    }

    true
  }

  private def hasDigits(string: String): Boolean = {
    string.exists(_.isDigit)
  }

  private def allDigits(string: String): Boolean = {
    string.forall(_.isDigit)
  }

  def routesWithStateTag(): Unit = {
    val routeRepository = new RouteRepositoryImpl(database)
    val allRouteIds = routeRepository.allRouteIds()
    allRouteIds.zipWithIndex.foreach { case (routeId, index) =>
      if (((index + 1) % 100) == 0) {
        println(s"${index + 1}/${allRouteIds.size}")
      }
      routeRepository.routeWithId(routeId).foreach { routeInfo =>
        if (routeInfo.tags.has("state")) {
          println(s"""== $routeId state=${routeInfo.tags("state").getOrElse("")}""")
        }
      }
    }
  }

  def blackListedRoutesThatAreInTheDatabase(): Unit = {
    val routeRepository = new RouteRepositoryImpl(database)
    val blackListRepository = new BlackListRepositoryImpl(database)
    val blackListedRouteIds = blackListRepository.get.routes.map(_.id)
    blackListedRouteIds.foreach { routeId =>
      routeRepository.routeWithId(routeId) match {
        case None => println(s"$routeId no")
        case Some(routeInfo) => println(s"$routeId yes")
          routeRepository.delete(Seq(routeId))
      }
    }
  }

  def printOrphanRoutes(): Unit = {
    val orphanRepository = new OrphanRepositoryImpl(database)
    println("nl orphan route count = " + orphanRepository.orphanRoutes(Subset.nlBicycle).size)
    println("be orphan route count = " + orphanRepository.orphanRoutes(Subset.beBicycle).size)
    println("de orphan route count = " + orphanRepository.orphanRoutes(Subset.deBicycle).size)
  }

  def blackListedRoutesThatWouldBeExcludedByTheNewRules(): Unit = {
    val routeRepository = new RouteRepositoryImpl(database)
    val blackListRepository = new BlackListRepositoryImpl(database)
    val blackListedRouteIds = blackListRepository.get.routes.map(_.id)

    blackListedRouteIds.foreach { routeId =>
      routeRepository.routeWithId(routeId) match {
        case None => println(s"route $routeId not in database")
        case Some(route) =>
          val redundantNodeCount = route.analysis.map.redundantNodes.size
          val uniqueRedundantNodeCount = route.analysis.map.redundantNodes.map(_.name).toSet.size
          val km = route.summary.meters / 1000
          println(s"route $routeId redundant nodes size = $redundantNodeCount ($uniqueRedundantNodeCount) $km")
      }
    }
  }

  def printLongestGermanBicycleRoutes(): Unit = {
    val orphanRepository = new OrphanRepositoryImpl(database)
    val routeRepository = new RouteRepositoryImpl(database)

    val routes = orphanRepository.orphanRoutes(Subset.deBicycle)

    println("route count = " + routes.size)

    //    case class RouteSummary(
    //      id: Long,
    //      country: Option[Country],
    //      networkType: NetworkType,
    //      name: String,
    //      meters: Int,
    //      isBroken: Boolean,
    //      wayCount: Int,
    //      timestamp: Timestamp,
    //      nodeNames: Seq[String],
    //      tags: Tags
    //    ) extends Tagable

    val sortedRoutes = routes.sortWith { (r1, r2) =>
      r1.meters > r2.meters
    }

    sortedRoutes.drop(10).filter(_.nodeNames.size > 1).filter(_.name == "no-name").zipWithIndex.foreach { case (route, index) =>
      println(s"${index + 1} id=${route.id}, name=${route.name}, length=${route.meters / 1000}, nodeCount=${route.nodeNames.size}")
    }

  }


  def removeBlacklistedNetworksAndRoutes(): Unit = {
    val blackListRepository = new BlackListRepositoryImpl(database)
    val networkRepository = new NetworkRepositoryImpl(database)
    val routeRepository = new RouteRepositoryImpl(database)

    val blackList = blackListRepository.get

    blackList.networks.foreach { networkEntry =>
      println(s"Delete blacklisted network ${networkEntry.id} - ${networkEntry.name}")
      networkRepository.delete(networkEntry.id)
    }

    blackList.routes.foreach { routeEntry =>
      println(s"Delete blacklisted route ${routeEntry.id} - ${routeEntry.name}")
      routeRepository.delete(Seq(routeEntry.id))
    }
  }

  def keyBasedAllRouteCount(): Unit = {
    val routeIds = DocumentView.allRouteIds(database)
    println(s"key based all route count=${routeIds.size}")
  }
}
