package kpn.database.actions.routes

import kpn.api.common.RouteType
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import kpn.server.analyzer.engine.tiles.domain.TileId
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal

object MongoQueryRouteTileDocs {
  private val log = Log(classOf[MongoQueryRouteTileDocs])
}

class MongoQueryRouteTileDocs(database: Database) {

  def execute(routeType: RouteType, tileId: TileId, log: Log = MongoQueryRouteTileDocs.log): Seq[RouteTileDoc] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("z", tileId.z),
            equal("x", tileId.x),
            equal("y", tileId.y),
            equal("routeTypes", routeType.entryName)
          )
        ),
      )
      val docs = database.routeTiles.aggregate[RouteTileDoc](pipeline, log)
      (s"${docs.size} tiles", docs)
    }
  }
}
