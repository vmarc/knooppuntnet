package kpn.database.actions.monitor

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.core.util.DebugLogger.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.MongoProjections.arraySize
import kpn.database.base.Types.MongoPipeline
import kpn.server.monitor.repository.MonitorStateDeviationInfo
import org.bson.Document
import org.bson.types.ObjectId

class MongoQueryMonitorStateDeviationInfos(database: Database) {
  def execute(routeId: ObjectId): Seq[MonitorStateDeviationInfo] = {
    val pipeline = buildPipeline(routeId)
    log.debugElapsed {
      val infos = database.monitorStates.aggregate(pipeline, classOf[MonitorStateDeviationInfo], log)
      (s"${infos.length} deviation infos", infos)
    }
  }

  private def buildPipeline(routeId: ObjectId): MongoPipeline = {
    Seq(
      filter(
        equal("routeId", routeId)
      ),
      project(
        fields(
          excludeId(),
          include("relationId"),
          computed("deviationDistance", Document.parse("""{ $sum: "$deviations.meters" }""")),
          arraySize("deviationCount", "$deviations"),
        )
      )
    )
  }
}
