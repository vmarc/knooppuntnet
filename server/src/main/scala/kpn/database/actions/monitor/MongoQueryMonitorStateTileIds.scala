package kpn.database.actions.monitor

import kpn.core.util.DebugLogger.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.tiles.domain.TileId
import org.mongodb.scala.Document
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

class MongoQueryMonitorStateTileIds(database: Database) {
  def execute(): Seq[TileId] = {
    val pipeline = buildPipeline()
    log.debugElapsed {
      val tileIds = database.monitorStateTiles.aggregate[TileId](pipeline, log)
      (s"${tileIds.length} state tile ids", tileIds)
    }
  }

  private def buildPipeline(): MongoPipeline = {
    Seq(
      group(
        Document(
          "z" -> "$z",
          "x" -> "$x",
          "y" -> "$y"
        ),
      ),
      project(
        fields(
          excludeId(),
          computed("z", "$_id.z"),
          computed("x", "$_id.x"),
          computed("y", "$_id.y"),
        )
      ),
      sort(
        orderBy(
          ascending("z"),
          ascending("x"),
          ascending("y"),
        )
      )
    )
  }
}
