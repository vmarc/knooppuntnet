package kpn.server.repository

import akka.util.Timeout
import kpn.core.db.KeyPrefix
import kpn.core.db.RouteDoc
import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.json.JsonFormats.routeDocFormat
import kpn.core.db.views.AnalyzerDesign
import kpn.core.db.views.ReferenceView
import kpn.core.util.Log
import kpn.shared.NetworkType
import kpn.shared.common.Reference
import kpn.shared.route.RouteInfo
import kpn.shared.route.RouteReferences
import org.springframework.stereotype.Component
import spray.json.JsValue

@Component
class RouteRepositoryImpl(mainDatabase: Database) extends RouteRepository {

  private val groupSize = 25
  private val log = Log(classOf[RouteRepository])

  override def save(routes: RouteInfo*): Unit = {
    log.debugElapsed {
      routes.toSeq.sliding(groupSize, groupSize).toSeq.foreach { groupRoutes =>
        saveRoutes(groupRoutes)
      }
      (s"Saved ${routes.size} routes overall", ())
    }
  }

  private def saveRoutes(routes: Seq[RouteInfo]): Unit = {

    log.debugElapsed {

      val existingRouteDocs = readExistingRouteDocs(routes)
      val (existingRoutes, addedRoutes) = partitionRoutes(routes, existingRouteDocs)
      val updatedRoutes = filterUpdatedRoutes(routes, existingRoutes)
      val unchangedRoutes = filterUnchangedRoutes(existingRoutes, updatedRoutes)
      val addedRouteDocs = createAddedRouteDocs(addedRoutes)
      val updatedRouteDocs = createUpdatedRouteDocs(existingRouteDocs, updatedRoutes)
      val docs = addedRouteDocs ++ updatedRouteDocs

      if (docs.nonEmpty) {
        mainDatabase.bulkSave(docs)
      }

      val message = s"Saved (${routes.size}) routes: " +
        Seq(
          msg("Unchanged", unchangedRoutes),
          msg("Added", addedRoutes),
          msg("Updated", updatedRoutes)
        ).flatten.mkString(" / ")

      (message, ())
    }
  }

  private def readExistingRouteDocs(routes: Seq[RouteInfo]): Seq[RouteDoc] = {
    val routeIds = routes.map(_.id)
    val routeDocIds = routeIds.map(docId)
    mainDatabase.objectsWithIds(routeDocIds, Couch.batchTimeout, stale = false).map(jsValue => routeDocFormat.read(jsValue))
  }

  private def partitionRoutes(routes: Seq[RouteInfo], existingRouteDocs: Seq[RouteDoc]): (Seq[RouteInfo], Seq[RouteInfo]) = {
    val existingRouteMap = existingRouteDocs.map(doc => doc.route.id -> doc.route).toMap
    val (existingRoutes, addedRoutes) = routes.partition(route => existingRouteMap.contains(route.id))
    (existingRoutes.map(route => existingRouteMap(route.id)), addedRoutes)
  }

  private def filterUpdatedRoutes(routes: Seq[RouteInfo], existingRoutes: Seq[RouteInfo]): Seq[RouteInfo] = {
    routes.filter { route =>
      existingRoutes.find(_.id == route.id) match {
        case Some(existingRoute) => existingRoute != route
        case None => false
      }
    }
  }

  private def filterUnchangedRoutes(existingRoutes: Seq[RouteInfo], updatedRoutes: Seq[RouteInfo]): Seq[RouteInfo] = {
    val updatedRouteIds = updatedRoutes.map(_.id)
    existingRoutes.filterNot { route =>
      updatedRouteIds.contains(route.id)
    }
  }

  private def createAddedRouteDocs(addedRoutes: Seq[RouteInfo]): Seq[JsValue] = {
    addedRoutes.map(route => routeDocFormat.write(RouteDoc(docId(route.id), route, None)))
  }

  private def createUpdatedRouteDocs(existingRouteDocs: Seq[RouteDoc], updatedRoutes: Seq[RouteInfo]): Seq[JsValue] = {
    updatedRoutes.flatMap { route =>
      existingRouteDocs.find(doc => doc.route.id == route.id) match {
        case Some(doc) =>
          val rev = doc._rev
          Some(routeDocFormat.write(RouteDoc(docId(route.id), route, rev)))
        case None => None
      }
    }
  }

  private def msg(title: String, routes: Seq[RouteInfo]): Option[String] = {
    if (routes.nonEmpty) {
      Some(s"$title (${routes.size}) " + routes.map(_.id).mkString(","))
    }
    else {
      None
    }
  }

  override def delete(routeIds: Seq[Long]): Unit = {
    val routeDocIds = routeIds.map(docId)
    mainDatabase.deleteDocs(routeDocIds)
  }

  override def routeWithId(routeId: Long, timeout: Timeout = Couch.defaultTimeout): Option[RouteInfo] = {
    mainDatabase.optionGet(docId(routeId), timeout).map(routeDocFormat.read).map(_.route)
  }

  override def routesWithIds(routeIds: Seq[Long], timeout: Timeout): Seq[RouteInfo] = {
    val ids = routeIds.map(id => docId(id))
    mainDatabase.objectsWithIds(ids, timeout, stale = false).map(doc => routeDocFormat.read(doc)).map(_.route)
  }

  override def routeReferences(routeId: Long, timeout: Timeout, stale: Boolean): RouteReferences = {
    val references = mainDatabase.query(AnalyzerDesign, ReferenceView, timeout, stale)("route", routeId).map(ReferenceView.convert).flatMap { row =>
      NetworkType.withName(row.referrerNetworkType).map { networkType =>
        row.referrerType -> Reference(
          row.referrerId,
          row.referrerName,
          networkType,
          row.connection
        )
      }
    }
    val networkReferences = references.filter(_._1 == "network").map(_._2).sorted
    RouteReferences(networkReferences)
  }

  override def filterKnown(routeIds: Set[Long]): Set[Long] = {
    log.debugElapsed {
      val existingRouteIds = routeIds.sliding(50, 50).flatMap { routeIdsSubset =>
        val routeDocIds = routeIdsSubset.map(docId).toSeq
        val existingRouteDocIds = mainDatabase.keysWithIds(routeDocIds)
        existingRouteDocIds.flatMap { routeDocId =>
          try {
            Some(java.lang.Long.parseLong(routeDocId.substring(KeyPrefix.Route.length + 1)))
          }
          catch {
            case e: NumberFormatException => None
          }
        }
      }.toSet
      (s"${existingRouteIds.size}/${routeIds.size} existing routes", existingRouteIds)
    }
  }

  private def docId(routeId: Long): String = {
    s"${KeyPrefix.Route}:$routeId"
  }
}
