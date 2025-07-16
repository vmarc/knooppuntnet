package kpn.database.actions.monitor

import kpn.core.util.DebugLogger.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.tiles.domain.TileId
import kpn.server.monitor.domain.MonitorReferenceTileInfo
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

class MongoQueryMonitorReferenceTiles(database: Database) {
  def execute(tileId: TileId): Seq[MonitorReferenceTileInfo] = {
    val pipeline = buildPipeline(tileId)
    log.infoElapsed {
      val tiles = database.monitorReferences.aggregate[MonitorReferenceTileInfo](pipeline, log)
      (s"${tiles.length} reference tiles", tiles)
    }
  }

  private def buildPipeline(tileId: TileId): MongoPipeline = {
    Seq(
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
