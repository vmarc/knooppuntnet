package kpn.database.actions.nodes

import kpn.api.common.RouteType
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileId
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryNodeTileInfo {
  private val log = Log(classOf[MongoQueryNodeTileInfo])

  private val projectNodeTileInfo: Bson = {
    project(
      fields(
        include("_id"),
        include("names"),
        include("latitude"),
        include("longitude"),
        include("lastSurvey"),
        include("tags"),
        include("facts")
      )
    )
  }
}

class MongoQueryNodeTileInfo(database: Database) {

  def execute(routeType: RouteType, tileId: TileId, log: Log = MongoQueryNodeTileInfo.log): Seq[NodeTileInfo] = {
    log.debugElapsed {
      val tilename = s"${routeType.entryName}-${tileId.name}"
      val pipeline = Seq(
        filter(
          and(
            equal("active", true),
            equal("labels", Label.routeType(routeType)),
            equal("tiles", tilename)
          )
        ),
        MongoQueryNodeTileInfo.projectNodeTileInfo
      )
      val nodes = database.nodes.aggregate[NodeTileInfo](pipeline, log)
      (s"${nodes.size} nodes", nodes)
    }
  }
}
