package kpn.database.actions.routes

import kpn.api.common.RouteType
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileId
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal

object MongoQueryRouteTileInfos {
  private val log = Log(classOf[MongoQueryRouteTileInfos])
}

class MongoQueryRouteTileInfos(database: Database) {

  def byZoomLevel(routeType: RouteType, zoomLevel: Int, log: Log = MongoQueryRouteTileInfos.log): Seq[RouteTileInfo] = {
    log.debugElapsed {
      val pipeline = buildByZoomLevelPipeline(routeType, zoomLevel)
      val routeTileInfos = database.routeTiles.aggregate[RouteTileInfo](pipeline, log)
      (s"${routeTileInfos.size} route tile infos", routeTileInfos)
    }
  }

  def byTileId(routeType: RouteType, tileId: TileId, log: Log = MongoQueryRouteTileInfos.log): Seq[RouteTileInfo] = {
    log.debugElapsed {
      val pipeline = buildByTileIdPipeline(routeType, tileId)
      val routeTileInfos = database.routeTiles.aggregate[RouteTileInfo](pipeline, log)
      (s"${routeTileInfos.size} route tile infos", routeTileInfos)
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
