package kpn.core.tools.monitor

import kpn.database.base.Database
import kpn.server.analyzer.engine.monitor.analysis.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.state.MonitorStateStore
import kpn.server.analyzer.engine.monitor.state.MonitorStateTileBuilder
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tile.RouteTileCache
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.route.update.MonitorUpdateAnalysis
import kpn.server.repository.RouteRepository

class MonitorUpdateToolConfiguration(database: Database) {

  val monitorGroupRepository: MonitorGroupRepository = new MonitorGroupRepository(database)
  val monitorRouteRepository: MonitorRouteRepository = new MonitorRouteRepository(database)

  val monitorUpdateAnalysis: MonitorUpdateAnalysis = {
    val routeRepository = new RouteRepository(database)
    val routeTileCache = new RouteTileCache()
    val lineSegmentTileCalculator = new LineSegmentTileCalculator(routeTileCache)
    val monitorStateTileBuilder = new MonitorStateTileBuilder(lineSegmentTileCalculator)
    val monitorStateStore = new MonitorStateStore(monitorRouteRepository, monitorStateTileBuilder)
    val monitorRouteDeviationAnalyzer = new MonitorRouteDeviationAnalyzer()
    new MonitorUpdateAnalysis(
      routeRepository,
      monitorRouteRepository,
      monitorRouteDeviationAnalyzer,
      monitorStateStore
    )
  }
}
