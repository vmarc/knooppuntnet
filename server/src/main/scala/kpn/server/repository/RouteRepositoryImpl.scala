package kpn.server.repository

import kpn.api.common.Bounds
import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.api.common.route.RouteMapInfo
import kpn.api.common.route.RouteNameInfo
import kpn.api.common.route.RouteSegmentData
import kpn.api.common.search.ConditionGroup
import kpn.api.common.search.RouteList
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.NetworkRouteDetail
import kpn.core.doc.ParentRouteData
import kpn.core.doc.RouteDoc
import kpn.core.doc.SubRouteData
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryBaseRouteIds
import kpn.database.actions.routes.MongoQueryKnownRouteIds
import kpn.database.actions.routes.MongoQueryNetworkRouteDetails
import kpn.database.actions.routes.MongoQueryParentRoutes
import kpn.database.actions.routes.MongoQueryRouteBounds
import kpn.database.actions.routes.MongoQueryRouteCountry
import kpn.database.actions.routes.MongoQueryRouteElementIds
import kpn.database.actions.routes.MongoQueryRouteIds
import kpn.database.actions.routes.MongoQueryRouteMapInfo
import kpn.database.actions.routes.MongoQueryRouteNameInfo
import kpn.database.actions.routes.MongoQueryRouteNetworkReferences
import kpn.database.actions.routes.MongoQueryRouteSearchResults
import kpn.database.actions.routes.MongoQueryRouteSegmentData
import kpn.database.actions.routes.MongoQueryRouteTileIds
import kpn.database.actions.routes.MongoQueryRouteTileInfos
import kpn.database.actions.routes.MongoQueryRoutes
import kpn.database.actions.routes.MongoQuerySubRouteData
import kpn.database.base.Database
import kpn.database.base.StringId
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileInfo
import kpn.server.analyzer.engine.changes.changes.ReferencedElementIds
import kpn.server.analyzer.engine.tiles.domain.TileId
import kpn.server.sync.Transaction
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
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

  override def activeBaseRouteIds(): Seq[Long] = {
    new MongoQueryBaseRouteIds(database).execute(log).sorted
  }

  override def tileIds(routeType: RouteType): Seq[TileId] = {
    new MongoQueryRouteTileIds(database).execute(routeType, log)
  }

  override def saveRoute(routeDoc: RouteDoc): Unit = {
    database.routes.save(routeDoc, log)
    database.transactions.save(Transaction.routeUpdate(routeDoc._id))
  }

  override def saveRouteTile(routeTileInfo: RouteTileInfo): Unit = {
    database.routeTiles.save(routeTileInfo, log)
  }

  override def routeTiles(routeId: Long): Seq[RouteTileInfo] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          equal("routeId", routeId),
        )
      )
      val docs = database.routeTiles.aggregate[RouteTileInfo](pipeline, log)
      (s"find tile docs route $routeId", docs)
    }
  }

  override def routeTileIds(routeId: Long): Seq[String] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          equal("routeId", routeId),
        ),
        project(
          fields(
            include("_id")
          )
        )
      )
      val ids = database.routeTiles.aggregate[StringId](pipeline, log).map(_._id)
      (s"found ${ids.size} tile doc ids for route $routeId", ids)
    }
  }

  override def tileInfosByZoomLevel(routeType: RouteType, zoomLevel: Int): Seq[RouteTileInfo] = {
    new MongoQueryRouteTileInfos(database).byZoomLevel(routeType, zoomLevel, log)
  }

  override def tileInfosByTileId(routeType: RouteType, tileId: TileId): Seq[RouteTileInfo] = {
    new MongoQueryRouteTileInfos(database).byTileId(routeType, tileId, log)
  }

  override def deleteRouteTiles(routeId: Long): Unit = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          equal("routeId", routeId),
        ),
        project(
          include("_id")
        )
      )
      val tileDocIds = database.routeTiles.aggregate[StringId](pipeline, log)
      tileDocIds.foreach { tileDocId =>
        database.routeTiles.deleteByStringId(tileDocId._id, log)
      }
      (s"delete tile docs route $routeId", ())
    }
  }

  override def deleteRouteTile(tileId: String): Unit = {
    log.debugElapsed {
      database.routeTiles.deleteByStringId(tileId, log)
      (s"delete route tile doc $tileId", ())
    }
  }

  override def bulkSaveRoutes(routeDocs: Seq[RouteDoc]): Unit = {
    database.routes.bulkSave(routeDocs, log)
    val transactions = routeDocs.map { routeDoc =>
      val transaction = Transaction.routeUpdate(routeDoc._id)
      database.transactions.save(transaction)
    }
  }

  override def findRouteById(routeId: Long): Option[RouteDoc] = {
    database.routes.findById(routeId, log)
  }

  override def routeSegments(routeId: Long): Option[RouteSegmentData] = {
    new MongoQueryRouteSegmentData(database).execute(routeId, log)
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

  override def routeCountry(routeId: Long): Option[Country] = {
    new MongoQueryRouteCountry(database).execute(routeId)
  }

  override def explore(query: ConditionGroup): RouteList = {
    val routeIds = new MongoQueryRoutes(database).execute(query)
    new MongoQueryRouteSearchResults(database).execute(routeIds)
  }

  override def activeRouteElementIds(): Seq[ReferencedElementIds] = {
    new MongoQueryRouteElementIds(database).execute()
  }

  override def saveBaseRoute(baseRoute: BaseRouteDoc): Unit = {
    database.baseRoutes.save(baseRoute, log)
  }

  override def bulkSaveBaseRoutes(baseRoutes: Seq[BaseRouteDoc]): Unit = {
    database.baseRoutes.bulkSave(baseRoutes, log)
  }

  override def findBaseRouteById(routeId: Long): Option[BaseRouteDoc] = {
    database.baseRoutes.findById(routeId, log)
  }

  override def filterKnownBaseRoutes(routeIds: Set[Long]): Set[Long] = {
    new MongoQueryKnownRouteIds(database).execute(routeIds.toSeq, log).toSet
  }

  override def bounds(routeIds: Seq[Long]): Option[Bounds] = {
    new MongoQueryRouteBounds(database).execute(routeIds, log)
  }

  override def subRouteData(routeId: Long): Option[SubRouteData] = {
    new MongoQuerySubRouteData(database).execute(routeId)
  }

  override def parentRoutes(routeId: Long): Seq[ParentRouteData] = {
    new MongoQueryParentRoutes(database).execute(routeId)
  }

  override def networkRouteDetails(routeIds: Seq[Long]): Seq[NetworkRouteDetail] = {
    new MongoQueryNetworkRouteDetails(database).execute(routeIds)
  }
}
