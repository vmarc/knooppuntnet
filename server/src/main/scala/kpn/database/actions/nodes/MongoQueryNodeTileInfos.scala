package kpn.database.actions.nodes

import kpn.api.common.RouteType
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

object MongoQueryNodeTileInfos {
  private val log = Log(classOf[MongoQueryNodeTileInfos])
}

class MongoQueryNodeTileInfos(database: Database) {

  def execute(routeType: RouteType, tileId: TileId, log: Log = MongoQueryNodeTileInfos.log): Seq[NodeTileInfo] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeType, tileId)
      val nodes = database.baseNodes.aggregate[NodeTileInfo](pipeline, log)
      (s"${nodes.size} nodes", nodes)
    }
  }

  private def buildPipeline(routeType: RouteType, tileId: TileId): MongoPipeline = {
    val tilename = s"${routeType.entryName}-${tileId.name}"
    Seq(
      filter(
        and(
          equal("active", true),
          equal("names.routeType", routeType.entryName),
          equal("tiles", tilename)
        )
      ),
      project(
        fields(
          include("_id"),
          include("names"),
          include("latitude"),
          include("longitude"),
          include("tags"),
          include("facts")
        )
      )
    )
  }
}
