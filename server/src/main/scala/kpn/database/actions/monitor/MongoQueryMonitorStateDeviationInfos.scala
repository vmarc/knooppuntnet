package kpn.database.actions.monitor

import kpn.api.base.ObjectId
import kpn.core.util.DebugLogger.log
import kpn.database.base.Database
import kpn.database.base.MongoProjections.arraySize
import kpn.database.base.Types.MongoPipeline
import kpn.server.monitor.repository.MonitorStateDeviationInfo
import org.mongodb.scala.Document
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

class MongoQueryMonitorStateDeviationInfos(database: Database) {
  def execute(routeId: ObjectId): Seq[MonitorStateDeviationInfo] = {
    val pipeline = buildPipeline(routeId)
    log.debugElapsed {
      val infos = database.monitorStates.aggregate[MonitorStateDeviationInfo](pipeline, log)
      (s"${infos.length} deviation infos", infos)
    }
  }

  private def buildPipeline(routeId: ObjectId): MongoPipeline = {
    Seq(
      filter(
        equal("routeId", routeId.raw)
      ),
      project(
        fields(
          excludeId(),
          include("relationId"),
          computed("deviationDistance", Document("""{ $sum: "$deviations.meters" }""")),
          arraySize("deviationCount", "$deviations"),
        )
      )
    )
  }
}
