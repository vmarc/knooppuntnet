package kpn.database.actions.routes

import kpn.api.common.RouteType
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal

object MongoQueryTileInfoRoutes {
  private val log = Log(classOf[MongoQueryTileInfoRoutes])
}

class MongoQueryTileInfoRoutes(database: Database) {

  def execute(routeType: RouteType, zoomLevel: Int, log: Log = MongoQueryTileInfoRoutes.log): Seq[RouteTileDoc] = {
    log.infoElapsed {
      val pipeline = buildPipeline(routeType, zoomLevel)
      val routeTileDocs = database.routeTiles.aggregate[RouteTileDoc](pipeline, log)
      (s"${routeTileDocs.size} route tile docs", routeTileDocs)
    }
  }

  private def buildPipeline(routeType: RouteType, zoomLevel: Int): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("routeTypes", routeType.entryName),
          equal("z", zoomLevel)
        )
      )
    )
  }
}
