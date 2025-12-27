package kpn.server.repository

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.common.RouteType
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryRouteTileIds
import kpn.database.actions.routes.MongoQueryRouteTileInfos
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.StringId
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileId
import org.springframework.stereotype.Component

@Component
class RouteTileRepository(database: Database) {

  private val log = Log(classOf[RouteTileRepository])

  def saveRouteTile(routeTileInfo: RouteTileInfo): Unit = {
    database.routeTiles.save(routeTileInfo, log)
  }

  def tileIds(routeType: RouteType): Seq[TileId] = {
    new MongoQueryRouteTileIds(database).execute(routeType, log)
  }

  def routeTiles(routeId: Long): Seq[RouteTileInfo] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          equal("routeId", routeId),
        )
      )
      val docs = database.routeTiles.aggregate(pipeline, classOf[RouteTileInfo], log)
      (s"find tile docs route $routeId", docs)
    }
  }

  def routeTileIds(routeId: Long): Seq[String] = {
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
      val ids = database.routeTiles.aggregate(pipeline, classOf[StringId], log).map(_._id)
      (s"found ${ids.size} tile doc ids for route $routeId", ids)
    }
  }

  def tileInfosByZoomLevel(routeType: RouteType, zoomLevel: Int): Seq[RouteTileInfo] = {
    new MongoQueryRouteTileInfos(database).byZoomLevel(routeType, zoomLevel, log)
  }

  def tileInfosByTileId(routeType: RouteType, tileId: TileId): Seq[RouteTileInfo] = {
    new MongoQueryRouteTileInfos(database).byTileId(routeType, tileId, log)
  }

  def deleteRouteTiles(routeId: Long): Unit = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          equal("routeId", routeId),
        ),
        project(
          include("_id")
        )
      )
      val tileDocIds = database.routeTiles.aggregate(pipeline, classOf[StringId], log)
      tileDocIds.foreach { tileDocId =>
        database.routeTiles.deleteByStringId(tileDocId._id, log)
      }
      (s"delete tile docs route $routeId", ())
    }
  }

  def deleteRouteTile(tileId: String): Unit = {
    log.debugElapsed {
      database.routeTiles.deleteByStringId(tileId, log)
      (s"delete route tile doc $tileId", ())
    }
  }
}
