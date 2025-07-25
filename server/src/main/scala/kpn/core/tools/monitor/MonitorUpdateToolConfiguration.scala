package kpn.core.tools.monitor

import kpn.database.base.Database
import kpn.server.analyzer.engine.monitor.analysis.MonitorRouteDeviationAnalyzerImpl
import kpn.server.analyzer.engine.monitor.state.MonitorStateStore
import kpn.server.analyzer.engine.monitor.state.MonitorStateTileBuilder
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculatorImpl
import kpn.server.analyzer.engine.tile.RouteTileCache
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorGroupRepositoryImpl
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl
import kpn.server.monitor.route.update.MonitorUpdateAnalysis
import kpn.server.repository.RouteRepositoryImpl

class MonitorUpdateToolConfiguration(database: Database) {

  val monitorGroupRepository: MonitorGroupRepository = new MonitorGroupRepositoryImpl(database)
  val monitorRouteRepository: MonitorRouteRepository = new MonitorRouteRepositoryImpl(database)

  val monitorUpdateAnalysis: MonitorUpdateAnalysis = {
    val routeRepository = new RouteRepositoryImpl(database)
    val routeTileCache = new RouteTileCache()
    val lineSegmentTileCalculator = new LineSegmentTileCalculatorImpl(routeTileCache)
    val monitorStateTileBuilder = new MonitorStateTileBuilder(lineSegmentTileCalculator)
    val monitorStateStore = new MonitorStateStore(monitorRouteRepository, monitorStateTileBuilder)
    val monitorRouteDeviationAnalyzer = new MonitorRouteDeviationAnalyzerImpl()
    new MonitorUpdateAnalysis(
      routeRepository,
      monitorRouteRepository,
      monitorRouteDeviationAnalyzer,
      monitorStateStore
    )
  }
}
