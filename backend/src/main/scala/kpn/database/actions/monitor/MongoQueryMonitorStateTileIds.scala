package kpn.database.actions.monitor

import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.core.util.DebugLogger.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.tiles.domain.TileId
import org.bson.Document

class MongoQueryMonitorStateTileIds(database: Database) {
  def execute(): Seq[TileId] = {
    val pipeline = buildPipeline()
    log.debugElapsed {
      val tileIds = database.monitorStateTiles.aggregate(pipeline, classOf[TileId], log)
      (s"${tileIds.length} state tile ids", tileIds)
    }
  }

  private def buildPipeline(): MongoPipeline = {
    Seq(
      group(
        new Document(
          java.util.Map.of(
            "z", "$z",
            "x", "$x",
            "y", "$y"
          )
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
