package kpn.server.repository

import kpn.api.common.common.Reference
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteMapInfo
import kpn.api.common.route.RouteNameInfo
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
class RouteRepositoryImpl(database: Database) extends RouteRepository {

  private val log = Log(classOf[RouteRepositoryImpl])

  override def allRouteIds(): Seq[Long] = {
    database.routes.ids(log)
  }

  override def activeRouteIds(): Seq[Long] = {
    new MongoQueryRouteIds(database).execute(log).sorted
  }

  override def activeRouteElementIds(): Seq[ReferencedElementIds] = {
    new MongoQueryRouteElementIds(database).execute()
  }

  override def save(routeInfo: RouteInfo): Unit = {
    database.routes.save(routeInfo, log)
  }

  override def bulkSave(routeInfos: Seq[RouteInfo]): Unit = {
    database.routes.bulkSave(routeInfos, log)
  }

  override def delete(routeId: Long): Unit = {
    database.routes.delete(routeId, log)
    // TODO MONGO should also delete references, changes, etc?
  }

  override def findById(routeId: Long): Option[RouteInfo] = {
    database.routes.findById(routeId, log)
  }

  override def mapInfo(routeId: Long): Option[RouteMapInfo] = {
    new MongoQueryRouteMapInfo(database).execute(routeId, log)
  }

  override def nameInfo(routeId: Long): Option[RouteNameInfo] = {
    new MongoQueryRouteNameInfo(database).execute(routeId, log)
  }

  override def networkReferences(routeId: Long): Seq[Reference] = {
    new MongoQueryRouteNetworkReferences(database).execute(routeId, log)
  }

  override def filterKnown(routeIds: Set[Long]): Set[Long] = {
    // TODO MONGO should implement through lookup elsewhere? probably not
    new MongoQueryKnownRouteIds(database).execute(routeIds.toSeq, log).toSet
  }
}
