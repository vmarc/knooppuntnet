package kpn.database.actions.routes

import kpn.api.common.RouteType
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import kpn.server.analyzer.engine.tiles.domain.TileId
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal

object MongoQueryTileInfoRoutes {
  private val log = Log(classOf[MongoQueryTileInfoRoutes])
}

class MongoQueryTileInfoRoutes(database: Database) {

  def byZoomLevel(routeType: RouteType, zoomLevel: Int, log: Log = MongoQueryTileInfoRoutes.log): Seq[RouteTileDoc] = {
    log.infoElapsed {
      val pipeline = buildByZoomLevelPipeline(routeType, zoomLevel)
      val routeTileDocs = database.routeTiles.aggregate[RouteTileDoc](pipeline, log)
      (s"${routeTileDocs.size} route tile docs", routeTileDocs)
    }
  }

  def byTileId(routeType: RouteType, tileId: TileId, log: Log = MongoQueryTileInfoRoutes.log): Seq[RouteTileDoc] = {
    log.infoElapsed {
      val pipeline = buildByTileIdPipeline(routeType, tileId)
      val routeTileDocs = database.routeTiles.aggregate[RouteTileDoc](pipeline, log)
      (s"${routeTileDocs.size} route tile docs", routeTileDocs)
    }
  }

  private def buildByZoomLevelPipeline(routeType: RouteType, zoomLevel: Int): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("routeTypes", routeType.entryName),
          equal("z", zoomLevel)
        )
      )
    )
  }

  private def buildByTileIdPipeline(routeType: RouteType, tileId: TileId): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("routeTypes", routeType.entryName),
          equal("z", tileId.z),
          equal("x", tileId.x),
          equal("y", tileId.y),
        )
      )
    )
  }
}
