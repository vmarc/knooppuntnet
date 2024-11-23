package kpn.database.actions.routes

import kpn.api.custom.NetworkType
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

  def execute(networkType: NetworkType, tileId: TileId, log: Log = MongoQueryRouteTileDocs.log): Seq[RouteTileDoc] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("z", tileId.z),
            equal("x", tileId.x),
            equal("y", tileId.y),
            equal("networkTypes", networkType.name)
          )
        ),
      )
      val docs = database.routeTiles.aggregate[RouteTileDoc](pipeline, log)
      (s"${docs.size} tiles", docs)
    }
  }
}
