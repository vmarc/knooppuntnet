package kpn.database.actions.nodes

import kpn.api.common.RouteType
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.regex
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryNodeTileInfos {
  private val log = Log(classOf[MongoQueryNodeTileInfos])
}

class MongoQueryNodeTileInfos(database: Database) {

  def execute(routeType: RouteType, zoomLevel: Int, log: Log = MongoQueryNodeTileInfos.log): Seq[NodeTileInfo] = {
    log.infoElapsed {
      val pipeline = buildPipeline(routeType, zoomLevel)
      val nodes = database.baseNodes.aggregate[NodeTileInfo](pipeline, log)
      (s"${nodes.size} node tile infos", nodes)
    }
  }

  private def buildPipeline(routeType: RouteType, zoomLevel: Int): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("names.routeType", routeType.entryName),
          regex("tiles", s"^${routeType.entryName}-$zoomLevel-"),
        )
      ),
      unwind("$tiles"),
      filter(
        regex("tiles", s"^${routeType.entryName}-$zoomLevel-"),
      ),
      project(
        fields(
          computed("tileName", tileName(routeType)),
          computed("nodeId", "$_id"),
          include("names"),
          include("latitude"),
          include("longitude"),
          include("tags"),
          include("facts")
        )
      )
    )
  }

  private def tileName(routeType: RouteType): Bson = {
    BsonDocument(s"""{$$substr: ["$$tiles", ${routeType.entryName.length + 1}, 99]}""")
  }
}
