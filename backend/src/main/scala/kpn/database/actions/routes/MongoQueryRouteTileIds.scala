package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Projections.computed
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
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.tiles.domain.TileId
import org.bson.Document

object MongoQueryRouteTileIds {
  private val log = Log(classOf[MongoQueryRouteTileIds])
}

class MongoQueryRouteTileIds(database: Database) {

  def execute(routeType: RouteType, log: Log = MongoQueryRouteTileIds.log): Seq[TileId] = {
    log.infoElapsed {
      val pipeline = buildPipeline(routeType)
      val tiles = database.routeTiles.aggregate(pipeline, classOf[TileId], log)
      (s"${tiles.size} route tile ids", tiles)
    }
  }

  private def buildPipeline(routeType: RouteType): MongoPipeline = {
    Seq(
      filter(
        equal("routeTypes", routeType.entryName)
      ),
      project(
        fields(
          exclude("_id"),
          include("z"),
          include("x"),
          include("y"),
        )
      ),
      group(
        new Document(
          java.util.Map.of(
            "z", "$z",
            "x", "$x",
            "y", "$y"
          )
        ),
      ),
      project(
        fields(
          exclude("_id"),
          computed("z", "$_id.z"),
          computed("x", "$_id.x"),
          computed("y", "$_id.y"),
        )
      ),
      sort(
        orderBy(
          ascending(
            "z",
            "x",
            "y",
          )
        )
      )
    )
  }
}
