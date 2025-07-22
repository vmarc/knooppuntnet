package kpn.database.actions.monitor

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteDeviationInfo
import kpn.core.util.DebugLogger.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Sorts.descending
import org.mongodb.scala.model.Sorts.orderBy

class MongoQueryMonitorDeviations(database: Database) {
  def execute(routeId: ObjectId): Seq[MonitorRouteDeviationInfo] = {
    val pipeline = buildPipeline(routeId)
    log.debugElapsed {
      val deviations = database.monitorStates.aggregate[MonitorRouteDeviationInfo](pipeline, log)
      val sorted = deviations.zipWithIndex.map { case (deviation, index) => deviation.copy(id = index + 1) }
      (s"${sorted.length} deviations", sorted)
    }
  }

  private def buildPipeline(routeId: ObjectId): MongoPipeline = {
    Seq(
      filter(
        equal("routeId", routeId.raw)
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
