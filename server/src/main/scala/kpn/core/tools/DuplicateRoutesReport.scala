package kpn.core.tools

import kpn.core.database.Database
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.db.couch.Couch
import kpn.server.repository.RouteRepositoryImpl
import kpn.shared.Country
import kpn.shared.NetworkType

object DuplicateRoutesReport {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("master1") { database =>
      new DuplicateRoutesReport(database).run()
    }
    println("Done")
  }
}

class DuplicateRoutesReport(database: Database) {

  case class RouteWays(country: Country, networkType: NetworkType, id: Long, name: String, alternate: Boolean, wayIds: Set[Long])

  case class Overlap(
    name: String,
    routeId1: Long,
    routeId2: Long,
    percentage: String,
    wayCount1: Int,
    wayCount2: Int,
    commonWayCount: Int,
    alternate1: Boolean,
    alternate2: Boolean
  )

  implicit def overlapOrdering: Ordering[Overlap] = Ordering.by(o => (o.name, o.routeId1, o.routeId2))

  def run(): Unit = {

    val routeIds = DocumentView.allRouteIds(database)
    val routes = loadRoutes(routeIds)

    Country.all.foreach { country =>
      NetworkType.all.foreach { networkType =>
        val subsetRoutes = routes.filter(_.country == country).filter(_.networkType == networkType)
        val overlaps = findOverlaps(subsetRoutes)
        if (overlaps.nonEmpty) {
          println()
          println(s"### ${country.domain}/${networkType.name} ${subsetRoutes.size} routes, with ${overlaps.size} overlaps")
          println()
          printTableHeader()
          overlaps.sorted.foreach(printOverlap)
        }
      }
    }
  }

  private def printTableHeader(): Unit = {
    println("|name|route 1|route 2|overlap|# ways 1|# ways 2|# shared|alt 1|alt 2|")
    println("|----|-------|-------|-------|--------|--------|--------|-----|-----|")
  }

  private def printOverlap(overlap: Overlap): Unit = {
    println(s"|${overlap.name}|${link(overlap.routeId1)}|${link(overlap.routeId2)}|${overlap.percentage}%|${overlap.wayCount1}|${overlap.wayCount2}|${
      overlap
        .commonWayCount
    }|${if (overlap.alternate1) "ALTERNATE" else ""}|${if (overlap.alternate2) "ALTERNATE" else ""}|")
  }

  private def link(routeId: Long): String = {
    s"[$routeId](http://knooppuntnet.nl/en/route/$routeId)"
  }

  private def findOverlaps(subsetRoutes: Seq[RouteWays]): Seq[Overlap] = {
    subsetRoutes.groupBy(_.name).flatMap { case (name, routes) =>
      routes.combinations(2).flatMap { case Seq(route1, route2) =>
        overlap(name, route1, route2)
      }
    }.toSeq
  }

  private def loadRoutes(routeIds: Seq[Long]): Seq[RouteWays] = {
    val routeRepository = new RouteRepositoryImpl(database)
    routeIds.zipWithIndex.flatMap { case (routeId, index) =>
      if (index % 100 == 0) {
        println(s"${routeIds.size}/$index")
      }
      routeRepository.routeWithId(routeId).flatMap { routeInfo =>
        val country = routeInfo.summary.country
        val networkType = routeInfo.summary.networkType
        val name = routeInfo.summary.name
        val wayIds = routeInfo.analysis.toSeq.flatMap(_.members.filter(_.isWay).map(_.id)).toSet
        val alternate = routeInfo.tags.has("state", "alternate")
        if (routeInfo.active && wayIds.nonEmpty && country.isDefined) {
          Some(RouteWays(country.get, networkType, routeInfo.id, name, alternate, wayIds))
        }
        else {
          None
        }
      }
    }
  }

  private def overlap(name: String, route1: RouteWays, route2: RouteWays): Option[Overlap] = {
    val totalWayCount = route1.wayIds.size + route2.wayIds.size
    val commonWayCount = route1.wayIds.intersect(route2.wayIds).size
    if (commonWayCount == 0 || totalWayCount == 0) {
      None
    }
    else {
      val percentageCommon = Math.round((100d * commonWayCount * 2) / totalWayCount).toString
      Some(
        Overlap(
          name,
          route1.id,
          route2.id,
          percentageCommon,
          route1.wayIds.size,
          route2.wayIds.size,
          commonWayCount,
          route1.alternate,
          route2.alternate
        )
      )
    }
  }

}
