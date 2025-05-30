package kpn.database.actions.nodes

import kpn.api.common.RouteType
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.StringId
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.tiles.domain.TileId
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.regex
import org.mongodb.scala.model.Projections.exclude
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

object MongoQueryNodeTileIds {
  private val log = Log(classOf[MongoQueryNodeTileIds])
}

class MongoQueryNodeTileIds(database: Database) {

  def execute(routeType: RouteType, log: Log = MongoQueryNodeTileIds.log): Seq[TileId] = {
    log.infoElapsed {
      val pipeline = buildPipeline(routeType)
      val tileNames = database.baseNodes.aggregate[StringId](pipeline, log)
      val tileIds = tileNames
        .map(_._id)
        .map { tileName =>
          val splitted = tileName.drop(routeType.entryName.length + 1).split("-")
          TileId(splitted(0).toInt, splitted(1).toInt, splitted(2).toInt)
        }
      (s"${tileIds.size} node tile ids", tileIds)
    }
  }

  private def buildPipeline(routeType: RouteType): MongoPipeline = {
    Seq(
      filter(
        equal("names.routeType", routeType.entryName),
      ),
      project(
        fields(
          exclude("_id"),
          include("tiles"),
        )
      ),
      unwind("$tiles"),
      filter(
        regex("tiles", s"^${routeType.entryName}-"),
      ),
      group("$tiles"),
      sort(orderBy(ascending("_id"))),
    )
  }
}
