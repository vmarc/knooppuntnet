package kpn.database.actions.monitor

import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.core.util.DebugLogger.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.tiles.domain.TileId
import kpn.server.monitor.domain.MonitorStateTile

class MongoQueryMonitorStateTiles(database: Database) {
  def execute(tileId: TileId): Seq[MonitorStateTile] = {
    val pipeline = buildPipeline(tileId)
    log.debugElapsed {
      val tiles = database.monitorStateTiles.aggregate(pipeline, classOf[MonitorStateTile], log)
      (s"${tiles.length} state tiles", tiles)
    }
  }

  private def buildPipeline(tileId: TileId): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("z", tileId.z),
          equal("x", tileId.x),
          equal("y", tileId.y),
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
