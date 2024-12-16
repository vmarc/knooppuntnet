package kpn.server.repository

import kpn.api.common.Bounds
import kpn.api.common.NetworkType
import kpn.core.doc.RouteDetailDoc
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryKnownRouteIds
import kpn.database.actions.routes.MongoQueryRouteBounds
import kpn.database.actions.routes.MongoQueryRouteElementIds
import kpn.database.actions.routes.MongoQueryRouteTileInfo
import kpn.database.base.Database
import kpn.server.analyzer.engine.changes.changes.ReferencedElementIds
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo
import org.springframework.stereotype.Component

@Component
class RouteDetailRepositoryImpl(database: Database) extends RouteDetailRepository {

  private val log = Log(classOf[RouteDetailRepositoryImpl])

  override def activeRouteElementIds(): Seq[ReferencedElementIds] = {
    new MongoQueryRouteElementIds(database).execute()
  }

  override def save(routeDetailDoc: RouteDetailDoc): Unit = {
    database.routeDetails.save(routeDetailDoc, log)
  }

  override def bulkSave(routeDetailDocs: Seq[RouteDetailDoc]): Unit = {
    database.routeDetails.bulkSave(routeDetailDocs, log)
  }

  override def findById(routeId: Long): Option[RouteDetailDoc] = {
    database.routeDetails.findById(routeId, log)
  }

  override def filterKnown(routeIds: Set[Long]): Set[Long] = {
    new MongoQueryKnownRouteIds(database).execute(routeIds.toSeq, log).toSet
  }

  override def routeTileInfosByNetworkType(networkType: NetworkType, nodeNetwork: Boolean): Seq[RouteTileInfo] = {
    new MongoQueryRouteTileInfo(database).findByNetworkType(networkType, nodeNetwork)
  }

  override def bounds(routeIds: Seq[Long]): Option[Bounds] = {
    new MongoQueryRouteBounds(database).execute(routeIds, log)
  }
}
