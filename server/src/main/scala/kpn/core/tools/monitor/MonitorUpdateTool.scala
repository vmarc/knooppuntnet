package kpn.core.tools.monitor

import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Options
import kpn.database.base.Tool
import kpn.database.util.Mongo
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.route.update.MonitorRouteRelationRepository
import kpn.server.monitor.route.update.MonitorRouteStructureLoader
import kpn.server.monitor.route.update.MonitorUpdaterConfiguration

object MonitorUpdateTool extends Tool[MonitorUpdateToolOptions] {
  private val log = Log(classOf[MonitorUpdateTool])

  override def options: Options[MonitorUpdateToolOptions] = MonitorUpdateToolOptions

  override def execute(options: MonitorUpdateToolOptions): Unit = {
    log.infoElapsed {
      Mongo.executeIn(options.databaseName) { database =>
        val tool = buildTool(options, database)
        // tool.update()
        tool.testUpdate("BE-GRV", "p01")
      }
      ("update completed", ())
    }
  }

  private def buildTool(options: MonitorUpdateToolOptions, database: Database): MonitorUpdateTool = {
    val overpassQueryExecutor = {
      if (options.remote) {
        new OverpassQueryExecutorRemoteImpl()
      }
      else {
        new OverpassQueryExecutorImpl()
      }
    }
    new MonitorUpdateTool(database, overpassQueryExecutor)
  }
}

class MonitorUpdateTool(
  database: Database,
  overpassQueryExecutor: OverpassQueryExecutor
) {

  private val monitorRouteRelationRepository = new MonitorRouteRelationRepository(overpassQueryExecutor)
  private val monitorRouteStructureLoader = new MonitorRouteStructureLoader(overpassQueryExecutor)
  private val configuration = new MonitorUpdaterConfiguration(
    database,
    monitorRouteRelationRepository,
    monitorRouteStructureLoader
  )

  def testUpdate(groupName: String, routeName: String): Unit = {
    configuration.monitorGroupRepository.groupByName(groupName) match {
      case None => MonitorUpdateTool.log.error(s"group not found: $groupName")
      case Some(group) =>
        configuration.monitorRouteRepository.routeByName(group._id, routeName) match {
          case None => MonitorUpdateTool.log.error(s"route not found: $groupName, $routeName")
          case Some(route) => updateAnalysis(group, route)
        }
    }
  }

  def update(): Unit = {
    val groups = configuration.monitorGroupRepository.groups().sortBy(_.name)
    groups.foreach { group =>
      configuration.monitorGroupRepository.groupRoutes(group._id).sortBy(_.name).foreach { route =>
        updateAnalysis(group, route)
      }
    }
  }

  private def updateAnalysis(group: MonitorGroup, route: MonitorRoute): Unit = {
    configuration.monitorUpdateAnalysis.updateAnalysis(group, route)
  }
}
