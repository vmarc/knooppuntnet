package kpn.server.repository

import kpn.api.common.common.Reference
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteMapInfo
import kpn.api.common.route.RouteNameInfo
import kpn.core.database.doc.CouchRouteDoc
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.database.views.analyzer.ReferenceView
import kpn.core.db.KeyPrefix
import kpn.core.mongo.Database
import kpn.core.mongo.actions.routes.MongoQueryKnownRouteIds
import kpn.core.mongo.actions.routes.MongoQueryRouteElementIds
import kpn.core.mongo.actions.routes.MongoQueryRouteIds
import kpn.core.mongo.actions.routes.MongoQueryRouteMapInfo
import kpn.core.mongo.actions.routes.MongoQueryRouteNameInfo
import kpn.core.mongo.actions.routes.MongoQueryRouteNetworkReferences
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.changes.ReferencedElementIds
import org.springframework.stereotype.Component

@Component
class RouteRepositoryImpl(
  database: Database,
  // old
  analysisDatabase: kpn.core.database.Database,
  mongoEnabled: Boolean
) extends RouteRepository {

  private val log = Log(classOf[RouteRepositoryImpl])

  override def allRouteIds(): Seq[Long] = {
    if (mongoEnabled) {
      database.routes.ids(log)
    }
    else {
      DocumentView.allRouteIds(analysisDatabase)
    }
  }

  override def activeRouteIds(): Seq[Long] = {
    new MongoQueryRouteIds(database).execute(log).sorted
  }

  override def activeRouteElementIds(): Seq[ReferencedElementIds] = {
    new MongoQueryRouteElementIds(database).execute()
  }

  override def save(routeInfo: RouteInfo): Unit = {
    if (mongoEnabled) {
      database.routes.save(routeInfo, log)
    }
    else {
      log.debugElapsed {
        analysisDatabase.save(CouchRouteDoc(docId(routeInfo.id), routeInfo))
        (s"Save route ${routeInfo.id}", ())
      }
    }
  }

  override def bulkSave(routeInfo: Seq[RouteInfo]): Unit = {
    database.routes.bulkSave(routeInfo, log)
  }

  override def delete(routeId: Long): Unit = {
    if (mongoEnabled) {
      database.routes.delete(routeId, log)
      // TODO MONGO should also delete references, changes, etc?
    }
    else {
      analysisDatabase.deleteDocWithId(docId(routeId))
    }
  }

  override def findById(routeId: Long): Option[RouteInfo] = {
    if (mongoEnabled) {
      database.routes.findById(routeId, log)
    }
    else {
      analysisDatabase.docWithId(docId(routeId), classOf[CouchRouteDoc]).map(_.route)
    }
  }

  override def mapInfo(routeId: Long): Option[RouteMapInfo] = {
    if (mongoEnabled) {
      new MongoQueryRouteMapInfo(database).execute(routeId, log)
    }
    else {
      throw new IllegalStateException("couchdb: method not supported")
    }
  }

  override def nameInfo(routeId: Long): Option[RouteNameInfo] = {
    if (mongoEnabled) {
      new MongoQueryRouteNameInfo(database).execute(routeId, log)
    }
    else {
      throw new IllegalStateException("couchdb: method not supported")
    }
  }

  override def networkReferences(routeId: Long, stale: Boolean): Seq[Reference] = {
    if (mongoEnabled) {
      new MongoQueryRouteNetworkReferences(database).execute(routeId, log)
    }
    else {
      val rows = ReferenceView.query(analysisDatabase, "route", routeId, stale)
      rows.filter(_.referrerType == "network").map(_.toReference).sorted
    }
  }

  override def filterKnown(routeIds: Set[Long]): Set[Long] = {
    if (mongoEnabled) {
      // TODO MONGO should implement through lookup elsewhere? probably not
      new MongoQueryKnownRouteIds(database).execute(routeIds.toSeq, log).toSet
    }
    else {
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
  }

  private def docId(routeId: Long): String = {
    s"${KeyPrefix.Route}:$routeId"
  }

}
