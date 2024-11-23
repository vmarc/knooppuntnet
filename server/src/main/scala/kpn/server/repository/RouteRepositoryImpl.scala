package kpn.server.repository

import kpn.api.common.common.Reference
import kpn.api.common.route.RouteMapInfo
import kpn.api.common.route.RouteNameInfo
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.core.doc.RouteDetailDoc
import kpn.core.doc.RouteDoc
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryKnownRouteIds
import kpn.database.actions.routes.MongoQueryRouteCountry
import kpn.database.actions.routes.MongoQueryRouteElementIds
import kpn.database.actions.routes.MongoQueryRouteIds
import kpn.database.actions.routes.MongoQueryRouteMapInfo
import kpn.database.actions.routes.MongoQueryRouteNameInfo
import kpn.database.actions.routes.MongoQueryRouteNetworkReferences
import kpn.database.actions.routes.MongoQueryRouteTileDocs
import kpn.database.actions.routes.MongoQueryRouteTileInfo
import kpn.database.actions.routes.MongoQueryRouteTileNames
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import kpn.server.analyzer.engine.changes.changes.ReferencedElementIds
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileId
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

  override def tiles(networkType: NetworkType): Seq[TileId] = {
    new MongoQueryRouteTileNames(database).execute(networkType, log)
  }

  override def tilesWithName(networkType: NetworkType, tileId: TileId): Seq[RouteTileDoc] = {
    new MongoQueryRouteTileDocs(database).execute(networkType, tileId, log)
  }

  override def saveRoute(routeDoc: RouteDoc): Unit = {
    database.routes.save(routeDoc, log)
  }

  override def saveRouteDetail(routeDetailDoc: RouteDetailDoc): Unit = {
    database.routeDetails.save(routeDetailDoc, log)
  }

  override def saveRouteTile(routeTileDoc: RouteTileDoc): Unit = {
    database.routeTiles.save(routeTileDoc, log)
  }

  override def bulkSaveRouteDetails(routeDetailDocs: Seq[RouteDetailDoc]): Unit = {
    database.routeDetails.bulkSave(routeDetailDocs, log)
  }

  override def bulkSaveRoutes(routeDocs: Seq[RouteDoc]): Unit = {
    database.routes.bulkSave(routeDocs, log)
  }

  override def findRouteById(routeId: Long): Option[RouteDoc] = {
    database.routes.findById(routeId, log)
  }

  override def findRouteDetailById(routeId: Long): Option[RouteDetailDoc] = {
    database.routeDetails.findById(routeId, log)
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
    new MongoQueryKnownRouteIds(database).execute(routeIds.toSeq, log).toSet
  }

  override def routeTileInfosByNetworkType(networkType: NetworkType, nodeNetwork: Boolean): Seq[RouteTileInfo] = {
    new MongoQueryRouteTileInfo(database).findByNetworkType(networkType, nodeNetwork)
  }

  override def routeTileInfosById(routeId: Long): Option[RouteTileInfo] = {
    new MongoQueryRouteTileInfo(database).findById(routeId)
  }

  override def routeCountry(routeId: Long): Option[Country] = {
    new MongoQueryRouteCountry(database).execute(routeId)
  }
}
