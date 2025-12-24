package kpn.database.actions.nodes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.elemMatch
import com.mongodb.client.model.Filters.regex
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.common.RouteType
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileId
import org.bson.BsonDocument
import org.bson.conversions.Bson

object MongoQueryNodeTileInfos {
  private val log = Log(classOf[MongoQueryNodeTileInfos])
}

class MongoQueryNodeTileInfos(database: Database) {

  def byZoomLevel(routeType: RouteType, zoomLevel: Int, log: Log = MongoQueryNodeTileInfos.log): Seq[NodeTileInfo] = {
    log.infoElapsed {
      val pipeline = buildByZoomLevelPipeline(routeType, zoomLevel)
      val nodes = database.baseNodes.aggregate(pipeline, classOf[NodeTileInfo], log)
      (s"${nodes.size} node tile infos", nodes)
    }
  }

  def byTileId(routeType: RouteType, tileId: TileId, log: Log = MongoQueryNodeTileInfos.log): Seq[NodeTileInfo] = {
    log.infoElapsed {
      val pipeline = buildByTileIdPipeline(routeType, tileId)
      val nodes = database.baseNodes.aggregate(pipeline, classOf[NodeTileInfo], log)
      (s"${nodes.size} node tile infos", nodes)
    }
  }

  private def buildByZoomLevelPipeline(routeType: RouteType, zoomLevel: Int): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          elemMatch("base.names", equal("routeType", routeType.entryName)),
          regex("tiles", s"^${routeType.entryName}-$zoomLevel-.*"),
        )
      ),
      unwind("$tiles"),
      filter(
        regex("tiles", s"^${routeType.entryName}-$zoomLevel-.*"),
      ),
      project(
        fields(
          excludeId(),
          computed("tileName", tileName(routeType)),
          computed("nodeId", "$_id"),
          computed("names", "$base.names"),
          computed("latitude", "$base.latitude"),
          computed("longitude", "$base.longitude"),
          computed("tags", "$base.raw.tags"),
          include("facts")
        )
      )
    )
  }

  private def buildByTileIdPipeline(routeType: RouteType, tileId: TileId): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          elemMatch("base.names", equal("routeType", routeType.entryName)),
          equal("tiles", s"${routeType.entryName}-${tileId.name}")
        )
      ),
      project(
        fields(
          computed("tileName", tileId.name),
          computed("nodeId", "$_id"),
          computed("names", "$base.names"),
          computed("latitude", "$base.latitude"),
          computed("longitude", "$base.longitude"),
          computed("tags", "$base.raw.tags"),
          include("facts")
        )
      )
    )
  }

  private def tileName(routeType: RouteType): Bson = {
    BsonDocument.parse(s"""{$$substr: ["$$tiles", ${routeType.entryName.length + 1}, 99]}""")
  }
}
