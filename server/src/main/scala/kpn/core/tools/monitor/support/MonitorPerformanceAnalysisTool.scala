package kpn.core.tools.monitor.support

import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.monitor.route.update.MonitorRouteRelationRepository
import kpn.server.monitor.route.update.MonitorRouteStructureLoader
import kpn.server.monitor.route.update.MonitorUpdateContext
import kpn.server.monitor.route.update.MonitorUpdaterConfiguration
import kpn.server.monitor.route.update.MonitorUpdateReporterLogger

object MonitorPerformanceAnalysisTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-monitor") { database =>
      new MonitorPerformanceAnalysisTool(database).update()
    }
  }
}

class MonitorPerformanceAnalysisTool(database: Database) {

  private val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
  private val monitorRouteRelationRepository = new MonitorRouteRelationRepository(overpassQueryExecutor)
  private val monitorRouteStructureLoader = new MonitorRouteStructureLoader(overpassQueryExecutor)

  private val configuration = new MonitorUpdaterConfiguration(
    database,
    monitorRouteRelationRepository,
    monitorRouteStructureLoader
  )

  def update(): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user",
        new MonitorUpdateReporterLogger(),
        MonitorRouteUpdate(
          action = "update",
          groupName = "AAA",
          routeName = "E2",
          referenceType = "osm",
          description = Some(""),
          relationId = Some(1254604),
          referenceTimestamp = Some(Timestamp(2023, 6, 1)),
        )
      )
    )
  }
}
