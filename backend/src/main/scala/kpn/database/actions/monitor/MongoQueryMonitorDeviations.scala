package kpn.database.actions.monitor

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Sorts.descending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.common.monitor.MonitorRouteDeviationInfo
import kpn.core.util.DebugLogger.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import org.bson.types.ObjectId

class MongoQueryMonitorDeviations(database: Database) {
  def execute(routeId: ObjectId): Seq[MonitorRouteDeviationInfo] = {
    val pipeline = buildPipeline(routeId)
    log.debugElapsed {
      val deviations = database.monitorStates.aggregate(pipeline, classOf[MonitorRouteDeviationInfo], log)
      val sorted = deviations.zipWithIndex.map { case (deviation, index) => deviation.copy(id = index + 1) }
      (s"${sorted.length} deviations", sorted)
    }
  }

  private def buildPipeline(routeId: ObjectId): MongoPipeline = {
    Seq(
      filter(
        equal("routeId", routeId)
      ),
      unwind("$deviations"),
      project(
        fields(
          excludeId(),
          computed("id", "$deviations.id"),
          computed("meters", "$deviations.meters"),
          computed("distance", "$deviations.distance"),
          computed("bounds", "$deviations.bounds"),
        )
      ),
      sort(
        orderBy(
          descending("meters")
        )
      )
    )
  }
}
