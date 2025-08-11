package kpn.database.actions.nodes

import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.regex
import com.mongodb.client.model.Projections.exclude
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.common.RouteType
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.StringId
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.tiles.domain.TileId

object MongoQueryNodeTileIds {
  private val log = Log(classOf[MongoQueryNodeTileIds])
}

class MongoQueryNodeTileIds(database: Database) {

  def execute(routeType: RouteType, log: Log = MongoQueryNodeTileIds.log): Seq[TileId] = {
    log.infoElapsed {
      val pipeline = buildPipeline(routeType)
      val tileNames = database.baseNodes.aggregate(pipeline, classOf[StringId], log)
      val tileIds = tileNames
        .map(_._id)
        .map { tileName =>
          val splitted = tileName.drop(routeType.toString.length + 1).split("-")
          TileId(splitted(0).toInt, splitted(1).toInt, splitted(2).toInt)
        }
      (s"${tileIds.size} node tile ids", tileIds)
    }
  }

  private def buildPipeline(routeType: RouteType): MongoPipeline = {
    Seq(
      filter(
        equal("names.routeType", routeType.toString),
      ),
      project(
        fields(
          exclude("_id"),
          include("tiles"),
        )
      ),
      unwind("$tiles"),
      filter(
        regex("tiles", s"^${routeType.toString}-"),
      ),
      group("$tiles"),
      sort(orderBy(ascending("_id"))),
    )
  }
}
