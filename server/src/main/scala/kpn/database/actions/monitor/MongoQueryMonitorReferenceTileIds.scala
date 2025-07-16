package kpn.database.actions.monitor

import kpn.core.util.DebugLogger.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.tiles.domain.TileId
import org.mongodb.scala.Document
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

class MongoQueryMonitorReferenceTileIds(database: Database) {
  def execute(): Seq[TileId] = {
    val pipeline = buildPipeline()
    log.infoElapsed {
      val tileIds = database.monitorReferences.aggregate[TileId](pipeline, log)
      (s"${tileIds.length} reference tile ids", tileIds)
    }
  }

  private def buildPipeline(): MongoPipeline = {
    Seq(
      unwind("$tiles"),
      project(
        fields(
          excludeId(),
          include("tiles.z"),
          include("tiles.x"),
          include("tiles.y"),
        )
      ),
      group(
        Document(
          "z" -> "$tiles.z",
          "x" -> "$tiles.x",
          "y" -> "$tiles.y"
        ),
      ),
      project(
        fields(
          excludeId(),
          computed("z", "$_id.z"),
          computed("x", "$_id.x"),
          computed("y", "$_id.y")
        )
      ),
      sort(
        orderBy(
          ascending("z"),
          ascending("x"),
          ascending("y")
        )
      )
    )
  }
}
