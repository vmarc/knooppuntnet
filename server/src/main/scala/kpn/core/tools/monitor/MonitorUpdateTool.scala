package kpn.core.tools.monitor

import kpn.core.tools.monitor.MonitorUpdateTool.log
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Options
import kpn.database.base.Tool
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzerImpl
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorGroupRepositoryImpl
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl
import kpn.server.monitor.route.update.MonitorUpdateAnalysis
import kpn.server.repository.RouteRepositoryImpl

object MonitorUpdateTool extends Tool[MonitorUpdateToolOptions] {
  private val log = Log(classOf[MonitorUpdateTool])

  override def options: Options[MonitorUpdateToolOptions] = MonitorUpdateToolOptions

  override def execute(options: MonitorUpdateToolOptions): Unit = {
    log.infoElapsed {
      Mongo.executeIn(options.databaseName) { database =>
        val tool = new MonitorUpdateTool(database)
        // tool.update()
        tool.testUpdate("BE-GRV", "p01")
      }
      ("update completed", ())
    }
  }
}

class MonitorUpdateTool(database: Database) {

  private val routeRepository = new RouteRepositoryImpl(database)
  private val monitorGroupRepository = new MonitorGroupRepositoryImpl(database)
  private val monitorRouteRepository = new MonitorRouteRepositoryImpl(database)
  private val monitorRouteDeviationAnalyzer = new MonitorRouteDeviationAnalyzerImpl()
  val monitorUpdateAnalysis = new MonitorUpdateAnalysis(
    routeRepository,
    monitorRouteRepository,
    monitorRouteDeviationAnalyzer
  )

  def testUpdate(groupName: String, routeName: String): Unit = {
    monitorGroupRepository.groupByName(groupName) match {
      case None => log.error(s"group not found: $groupName")
      case Some(group) =>
        monitorRouteRepository.routeByName(group._id, routeName) match {
          case None => log.error(s"route not found: $groupName, $routeName")
          case Some(route) => updateAnalysis(group, route)
        }
    }
  }

  def update(): Unit = {
    val groups = monitorGroupRepository.groups().sortBy(_.name)
    groups.foreach { group =>
      monitorGroupRepository.groupRoutes(group._id).sortBy(_.name).foreach { route =>
        updateAnalysis(group, route)
      }
    }
  }

  private def updateAnalysis(group: MonitorGroup, route: MonitorRoute): Unit = {
    Log.context(s"${group.name}, ${route.name}") {
      log.infoElapsed {
        monitorUpdateAnalysis.updateAnalysis(route)
        ("analysis completed", ())
      }
    }
  }
}
