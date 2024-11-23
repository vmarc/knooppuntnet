package kpn.database.actions.routes

import kpn.api.custom.NetworkType
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.analyzer.engine.tiles.domain.TileId
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.exclude
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryRouteTileNames {
  private val log = Log(classOf[MongoQueryRouteTileNames])
}

class MongoQueryRouteTileNames(database: Database) {

  def execute(networkType: NetworkType, log: Log = MongoQueryRouteTileNames.log): Seq[TileId] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          equal("networkTypes", networkType.name)
        ),
        project(
          fields(
            exclude("_id"),
            include("z"),
            include("x"),
            include("y"),
          )
        ),
        // TODO redesign tiles - group by z,x,y to avoid having to do the 'distinct' further down
        //        group(
        //          "$tile"
        //        ),
      )
      val tiles = database.routeTiles.aggregate[TileId](pipeline, log).distinct
      (s"${tiles.size} tiles", tiles)
    }
  }
}
