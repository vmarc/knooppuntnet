package kpn.server.repository

import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteReferences
import kpn.core.database.Database
import kpn.core.database.doc.RouteDoc
import kpn.core.database.views.analyzer.ReferenceView
import kpn.core.db.KeyPrefix
import kpn.core.db.RouteDocViewResult
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.location.RouteLocator
import org.springframework.stereotype.Component

@Component
class RouteRepositoryImpl(
  analysisDatabase: Database,
  routeLocator: RouteLocator
) extends RouteRepository {

  private val groupSize = 20
  private val log = Log(classOf[RouteRepository])

  override def save(routeInfo: RouteInfo): Unit = {
    log.debugElapsed {
      val comment = analysisDatabase.docWithId(docId(routeInfo.id), classOf[RouteDoc]) match {
        case Some(oldRouteDoc) =>
          updateRoute(oldRouteDoc, routeInfo)
        case None =>
          saveNewRoute(routeInfo)
      }
      (s"Save route ${routeInfo.id} $comment", ())
    }
  }

  private def saveNewRoute(routeInfo: RouteInfo): String = {
    routeInfo.analysis match {
      case Some(analysis) =>
        val routeLocationAnalysis = routeLocator.locate(routeInfo)
        val newAnalysis = analysis.copy(locationAnalysis = Some(routeLocationAnalysis))
        doSaveNewRoute(routeInfo.copy(analysis = Some(newAnalysis)))
        s"new, location calculated"
      case None =>
        doSaveNewRoute(routeInfo)
        s"new, without location"
    }
  }

  private def doSaveNewRoute(routeInfo: RouteInfo): Unit = {
    val doc = RouteDoc(docId(routeInfo.id), routeInfo)
    analysisDatabase.save(doc)
  }

  private def updateRoute(oldRouteDoc: RouteDoc, newRouteInfo: RouteInfo): String = {

    oldRouteDoc.route.analysis match {
      case Some(oldAnalysis) =>
        newRouteInfo.analysis match {
          case Some(newAnalysis) =>
            if (oldAnalysis.geometryDigest == newAnalysis.geometryDigest) {
              val analysisWithLocationInfo = newAnalysis.copy(locationAnalysis = oldAnalysis.locationAnalysis)
              val updated = newRouteInfo.copy(analysis = Some(analysisWithLocationInfo))
              "location re-used" + doUpdateRoute(oldRouteDoc, updated)
            }
            else {
              val routeLocationAnalysis = routeLocator.locate(newRouteInfo)
              val analysisWithLocationInfo = newAnalysis.copy(locationAnalysis = Some(routeLocationAnalysis))
              val updated = newRouteInfo.copy(analysis = Some(analysisWithLocationInfo))
              "location calculated" + doUpdateRoute(oldRouteDoc, updated)
            }

          case None =>
            // old route did have analysis, but new route does not
            "without location" + doUpdateRoute(oldRouteDoc, newRouteInfo)
        }

      case None =>

        newRouteInfo.analysis match {
          case Some(newAnalysis) =>
            // old route did not have analysis, but new route does have analysis, need to calculate location
            val routeLocationAnalysis = routeLocator.locate(newRouteInfo)
            val analysisWithLocationInfo = newAnalysis.copy(locationAnalysis = Some(routeLocationAnalysis))
            val updated = newRouteInfo.copy(analysis = Some(analysisWithLocationInfo))
            "location calculated" + doUpdateRoute(oldRouteDoc, updated)
          case None =>
            "without location" + doUpdateRoute(oldRouteDoc, newRouteInfo)
        }
    }
  }

  private def doUpdateRoute(oldRouteDoc: RouteDoc, newRouteInfo: RouteInfo): String = {
    if (newRouteInfo != oldRouteDoc.route) {
      val doc = RouteDoc(docId(newRouteInfo.id), newRouteInfo, oldRouteDoc._rev)
      analysisDatabase.save(doc)
      " (changed)"
    }
    else {
      " (no change)"
    }
  }

  def oldSave(routes: RouteInfo*): Unit = {
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

  override def routeWithId(routeId: Long): Option[RouteInfo] = {
    analysisDatabase.docWithId(docId(routeId), classOf[RouteDoc]).map(_.route)
  }

  override def routesWithIds(routeIds: Seq[Long]): Seq[RouteInfo] = {
    val ids = routeIds.map(id => docId(id))
    analysisDatabase.docsWithIds(ids, classOf[RouteDocViewResult], stale = false).rows.flatMap(_.doc.map(_.route))
  }

  override def routeReferences(routeId: Long, stale: Boolean): RouteReferences = {
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
