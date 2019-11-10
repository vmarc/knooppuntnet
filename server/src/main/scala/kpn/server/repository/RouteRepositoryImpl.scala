package kpn.server.repository

import akka.util.Timeout
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteReferences
import kpn.core.database.Database
import kpn.core.database.doc.RouteDoc
import kpn.core.database.views.analyzer.ReferenceView
import kpn.core.db.KeyPrefix
import kpn.core.db.RouteDocViewResult
import kpn.core.db.couch.Couch
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class RouteRepositoryImpl(analysisDatabase: Database) extends RouteRepository {

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
      val addedRouteDocs = addedRoutes.map(route => RouteDoc(docId(route.id), route))
      val updatedRouteDocs = createUpdatedRouteDocs(existingRouteDocs, updatedRoutes)
      val docs = addedRouteDocs ++ updatedRouteDocs

      if (docs.nonEmpty) {
        analysisDatabase.bulkSave(docs)
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
    analysisDatabase.docsWithIds(routeDocIds, classOf[RouteDocViewResult], stale = false).rows.flatMap(_.doc)
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

  private def createUpdatedRouteDocs(existingRouteDocs: Seq[RouteDoc], updatedRoutes: Seq[RouteInfo]): Seq[RouteDoc] = {
    updatedRoutes.flatMap { route =>
      existingRouteDocs.find(doc => doc.route.id == route.id) match {
        case Some(doc) =>
          val rev = doc._rev
          Some(RouteDoc(docId(route.id), route, rev))
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
    analysisDatabase.deleteDocsWithIds(routeDocIds)
  }

  override def routeWithId(routeId: Long, timeout: Timeout = Couch.defaultTimeout): Option[RouteInfo] = {
    analysisDatabase.docWithId(docId(routeId), classOf[RouteDoc]).map(_.route)
  }

  override def routesWithIds(routeIds: Seq[Long], timeout: Timeout): Seq[RouteInfo] = {
    val ids = routeIds.map(id => docId(id))
    analysisDatabase.docsWithIds(ids, classOf[RouteDocViewResult], stale = false).rows.flatMap(_.doc.map(_.route))
  }

  override def routeReferences(routeId: Long, timeout: Timeout, stale: Boolean): RouteReferences = {
    val rows = ReferenceView.query(analysisDatabase, "route", routeId, stale)
    val networkReferences = rows.filter(_.referrerType == "network").map(_.toReference).sorted
    RouteReferences(networkReferences)
  }

  override def filterKnown(routeIds: Set[Long]): Set[Long] = {
    log.debugElapsed {
      val existingRouteIds = routeIds.sliding(50, 50).flatMap { routeIdsSubset =>
        val routeDocIds = routeIdsSubset.map(docId).toSeq
        val existingRouteDocIds = analysisDatabase.keysWithIds(routeDocIds)
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
