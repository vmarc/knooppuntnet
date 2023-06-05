package kpn.core.tools.monitor

import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.monitor.route.MonitorRouteRelationRepository
import kpn.server.monitor.route.MonitorRouteStructureLoader
import kpn.server.monitor.route.MonitorUpdaterConfiguration

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
    configuration.monitorUpdater.update("user", "AAA", "E2", MonitorRouteProperties(
      groupName = "AAA",
      name = "E2",
      description = "",
      None,
      relationId = Some(1254604),
      referenceType = "osm",
      referenceTimestamp = Some(Timestamp(2023, 6, 2)),
      referenceFilename = None,
      referenceFileChanged = false
    ))
  }

  def analyze(): Unit = {
    configuration.monitorGroupRepository.groupByName("AAA") match {
      case None => println("group not found")
      case Some(group) =>
        configuration.monitorRouteRepository.routeByName(group._id, "G9") match {
          case None => println("route not found")
          case Some(route) => configuration.monitorUpdater.analyzeAll(group, route._id)
        }
    }
  }
}
