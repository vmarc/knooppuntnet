package kpn.database.actions.nodes

import kpn.api.common.NetworkType
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.StringId
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.tiles.domain.TileId
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.exclude
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

object MongoQueryNodeTilenames {
  private val log = Log(classOf[MongoQueryNodeTilenames])
}

class MongoQueryNodeTilenames(database: Database) {

  def execute(networkType: NetworkType, log: Log = MongoQueryNodeTilenames.log): Seq[TileId] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          equal("labels", Label.networkType(networkType)),
        ),
        project(
          fields(
            exclude("_id"),
            include("tiles"),
          )
        ),
        unwind("$tiles"),
        group("$tiles"),
        sort(orderBy(ascending("_id"))),
      )

      println(Mongo.pipelineString(pipeline))

      val tiles = database.nodes.aggregate[StringId](pipeline, log, allowDiskUse = true)
      val tileIds = tiles.map(_._id).filter(_.startsWith(networkType.entryName)).map { tileName =>
        val splitted = tileName.drop(networkType.entryName.length + 1).split("-")
        TileId(splitted(0).toInt, splitted(0).toInt, splitted(0).toInt)
      }
      (s"${tileIds.size} tiles", tileIds)
    }
  }
}
