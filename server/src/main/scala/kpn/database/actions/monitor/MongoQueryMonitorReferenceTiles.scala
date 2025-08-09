package kpn.database.actions.monitor

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.core.util.DebugLogger.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.tiles.domain.TileId
import kpn.server.monitor.domain.MonitorReferenceTileInfo

class MongoQueryMonitorReferenceTiles(database: Database) {
  def execute(tileId: TileId): Seq[MonitorReferenceTileInfo] = {
    val pipeline = buildPipeline(tileId)
    log.debugElapsed {
      val tiles = database.monitorReferences.aggregate(pipeline, classOf[MonitorReferenceTileInfo], log)
      (s"${tiles.length} reference tiles", tiles)
    }
  }

  private def buildPipeline(tileId: TileId): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("tiles.z", tileId.z),
          equal("tiles.x", tileId.x),
          equal("tiles.y", tileId.y),
        )
      ),
      unwind("$tiles"),
      filter(
        and(
          equal("tiles.z", tileId.z),
          equal("tiles.x", tileId.x),
          equal("tiles.y", tileId.y),
        )
      ),
      project(
        fields(
          excludeId(),
          include("routeId"),
          include("relationId"),
          computed("z", "$tiles.z"),
          computed("x", "$tiles.x"),
          computed("y", "$tiles.y"),
          computed("lines", "$tiles.lines"),
        )
      ),
      sort(
        orderBy(
          ascending("routeId"),
          ascending("relationId"),
        )
      )
    )
  }
}
