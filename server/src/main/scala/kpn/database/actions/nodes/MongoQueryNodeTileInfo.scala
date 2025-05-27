package kpn.database.actions.nodes

import kpn.api.common.RouteType
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileId
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryNodeTileInfo {
  private val log = Log(classOf[MongoQueryNodeTileInfo])
}

class MongoQueryNodeTileInfo(database: Database) {

  def execute(routeType: RouteType, tileId: TileId, log: Log = MongoQueryNodeTileInfo.log): Seq[NodeTileInfo] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeType, tileId)
      val nodes = database.nodes.aggregate[NodeTileInfo](pipeline, log)
      (s"${nodes.size} nodes", nodes)
    }
  }

  private def buildPipeline(routeType: RouteType, tileId: TileId): MongoPipeline = {
    val tilename = s"${routeType.entryName}-${tileId.name}"
    Seq(
      filter(
        and(
          equal("active", true),
          equal("labels", Label.routeType(routeType)),
          equal("tiles", tilename)
        )
      ),
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
    )
  }
}
