package kpn.database.actions.monitor

import kpn.core.util.DebugLogger.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.tiles.domain.TileId
import kpn.server.monitor.domain.MonitorStateTile
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

class MongoQueryMonitorStateTiles(database: Database) {
  def execute(tileId: TileId): Seq[MonitorStateTile] = {
    val pipeline = buildPipeline(tileId)
    log.infoElapsed {
      val tiles = database.monitorStateTiles.aggregate[MonitorStateTile](pipeline, log)
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
