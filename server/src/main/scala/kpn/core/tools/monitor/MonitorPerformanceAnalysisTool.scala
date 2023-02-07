package kpn.core.tools.monitor

import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.monitor.route.MonitorRouteRelationRepository
import kpn.server.monitor.route.MonitorUpdaterConfiguration

object MonitorPerformanceAnalysisTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-experimental") { database =>
      new MonitorPerformanceAnalysisTool(database).analyze()
    }
  }
}

class MonitorPerformanceAnalysisTool(database: Database) {

  private val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
  private val monitorRouteRelationRepository = new MonitorRouteRelationRepository(overpassQueryExecutor)
  private val configuration = new MonitorUpdaterConfiguration(database, monitorRouteRelationRepository)

  def analyze(): Unit = {
    configuration.monitorGroupRepository.groupByName("AAA") match {
      case None => println("group not found")
      case Some(group) =>
        configuration.monitorRouteRepository.routeByName(group._id, "Hexatrek") match {
          case None => println("route not found")
          case Some(route) => configuration.monitorUpdater.analyzeAll(group, route._id)
        }
    }
  }
}
