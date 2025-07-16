package kpn.database.actions.monitor

import kpn.core.util.DebugLogger.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.server.monitor.domain.MonitorRouteInfo
import org.mongodb.scala.model.Aggregates.lookup
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

class MongoQueryMonitorRouteInfos(database: Database) {
  def execute(): Seq[MonitorRouteInfo] = {
    val pipeline = buildPipeline()
    log.infoElapsed {
      val routes = database.monitorRoutes.aggregate[MonitorRouteInfo](pipeline, log)
      (s"${routes.length} monitor routes", routes)
    }
  }

  private def buildPipeline(): MongoPipeline = {
    Seq(
      lookup(
        "monitor-groups",
        "groupId",
        "_id",
        "group"
      ),
      unwind("$group"),
      project(
        fields(
          computed("groupName", "$group.name"),
          computed("routeName", "$name"),
        ),
      ),
      sort(
        orderBy(
          ascending("groupName"),
          ascending("routeName"),
        )
      )
    )
  }
}
