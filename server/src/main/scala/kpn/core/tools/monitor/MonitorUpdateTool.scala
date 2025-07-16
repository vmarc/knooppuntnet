package kpn.core.tools.monitor

import kpn.core.tools.monitor.MonitorUpdateTool.log
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Options
import kpn.database.base.Tool
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzerImpl
import kpn.server.analyzer.engine.monitor.state.MonitorStateStore
import kpn.server.analyzer.engine.monitor.state.MonitorStateTileBuilder
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculatorImpl
import kpn.server.analyzer.engine.tile.RouteTileCache
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
        tool.update()
        // tool.testUpdate("eu-icn-EV", "EV1-gpx")
      }
      ("update completed", ())
    }
  }
}

class MonitorUpdateTool(database: Database) {

  private val routeRepository = new RouteRepositoryImpl(database)
  private val monitorGroupRepository = new MonitorGroupRepositoryImpl(database)
  private val monitorRouteRepository = new MonitorRouteRepositoryImpl(database)
  private val routeTileCache = new RouteTileCache()
  private val lineSegmentTileCalculator = new LineSegmentTileCalculatorImpl(routeTileCache)
  private val monitorStateTileBuilder = new MonitorStateTileBuilder(lineSegmentTileCalculator)
  private val monitorStateStore = new MonitorStateStore(monitorRouteRepository, monitorStateTileBuilder)
  private val monitorRouteDeviationAnalyzer = new MonitorRouteDeviationAnalyzerImpl()
  val monitorUpdateAnalysis = new MonitorUpdateAnalysis(
    routeRepository,
    monitorRouteRepository,
    monitorRouteDeviationAnalyzer,
    monitorStateStore
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
        try {
          monitorUpdateAnalysis.updateAnalysis(route)
        }
        catch {
          case e: Exception =>
            log.error(e.getMessage, e)
            throw new RuntimeException(s"Error analyzing route ${group.name}/${route.name}", e)
        }
        ("analysis completed", ())
      }
    }
  }
}
