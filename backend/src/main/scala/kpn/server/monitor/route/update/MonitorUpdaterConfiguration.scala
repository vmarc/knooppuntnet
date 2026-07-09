package kpn.server.monitor.route.update

import kpn.database.base.Database
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.analyzer.engine.monitor.analysis.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.state.MonitorStateStore
import kpn.server.analyzer.engine.monitor.state.MonitorStateTileBuilder
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tile.RouteTileCache
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository

class MonitorUpdaterConfiguration(
  database: Database,
  val monitorRouteRelationRepository: MonitorRouteRelationRepository,
  val monitorRouteStructureLoader: MonitorRouteStructureLoader
) {
  val routeRepository = new RouteRepository(database)

  val monitorGroupRepository = new MonitorGroupRepository(database)
  val monitorRouteRepository = new MonitorRouteRepository(database)

  private val routeTileCache = new RouteTileCache()
  private val lineSegmentTileCalculator = new LineSegmentTileCalculator(routeTileCache)

  private val monitorRouteOsmSegmentAnalyzer = new MonitorRouteOsmSegmentAnalyzer()
  private val monitorRouteDeviationAnalyzer = new MonitorRouteDeviationAnalyzer()
  private val monitorRouteGapAnalyzer = new MonitorRouteGapAnalyzer()

  private val monitorUpdateCommon = new MonitorUpdateCommon(
    routeRepository,
    monitorGroupRepository,
    monitorRouteRepository
  )

  private val monitorReferenceBuilder = new MonitorReferenceBuilder(lineSegmentTileCalculator)

  private val monitorStateTileBuilder = new MonitorStateTileBuilder(lineSegmentTileCalculator)
  private val monitorStateStore = new MonitorStateStore(monitorRouteRepository, monitorStateTileBuilder)

  private val monitorUpdateGpxUpload = new MonitorGpxUpload(
    routeRepository,
    monitorRouteRepository,
    monitorUpdateCommon,
    monitorRouteDeviationAnalyzer,
    monitorReferenceBuilder,
    monitorStateStore
  )

  private val monitorUpdateGpxDelete = new MonitorGpxDelete(
    monitorRouteRepository,
    monitorUpdateCommon
  )

  private val monitorOsmAnalyze = new MonitorOsmAnalyze(
    routeRepository,
    monitorRouteRepository,
    monitorRouteStructureLoader,
    monitorRouteRelationRepository,
    monitorUpdateCommon,
    monitorRouteOsmSegmentAnalyzer,
    monitorRouteDeviationAnalyzer,
    monitorReferenceBuilder,
    monitorStateStore
  )

  private val monitorOsmAdd = new MonitorOsmAdd(
    monitorRouteRepository,
    monitorOsmAnalyze
  )

  private val monitorOsmUpdate = new MonitorOsmUpdate(
    monitorRouteRepository,
    monitorOsmAnalyze
  )

  private val monitorGpxAnalyze = new MonitorGpxAnalyze(
    routeRepository,
    monitorRouteRepository,
    monitorUpdateCommon,
    monitorOsmAnalyze,
    monitorRouteDeviationAnalyzer,
    monitorReferenceBuilder,
    monitorStateStore
  )

  private val monitorGpxUpdate = new MonitorGpxUpdate(
    routeRepository,
    monitorRouteRepository,
    monitorGpxAnalyze,
    monitorRouteDeviationAnalyzer,
    monitorReferenceBuilder,
    monitorStateStore
  )

  private val monitorOsmNowAnalyze = new MonitorOsmNowAnalyze(
    routeRepository,
    monitorRouteRepository,
    monitorUpdateCommon,
    monitorReferenceBuilder,
    monitorStateStore
  )

  private val monitorOsmNowUpdate = new MonitorOsmNowUpdate(
    monitorRouteRepository,
    monitorOsmNowAnalyze
  )

  private val monitorMultiGpxUpdate = new MonitorMultiGpxUpdate(
    routeRepository,
    monitorRouteRepository,
    monitorUpdateCommon
  )

  private val monitorUpdateUpdate = new MonitorUpdate(
    monitorGroupRepository,
    monitorRouteRepository,
    monitorUpdateCommon,
    monitorOsmUpdate,
    monitorOsmNowUpdate,
    monitorGpxUpdate,
    monitorMultiGpxUpdate,
    monitorStateStore
  )

  private val monitorUpdateAddMultiGpx = new MonitorMultiGpxAdd(
    routeRepository,
    monitorRouteRepository,
    monitorUpdateCommon
  )

  private val monitorUpdateAddOsmNow = new MonitorOsmNowAdd(
    routeRepository,
    monitorRouteRepository,
    monitorUpdateCommon,
    monitorOsmNowAnalyze
  )

  private val monitorGpxAdd = new MonitorGpxAdd(
    monitorGpxAnalyze
  )

  private val monitorUpdateAdd = new MonitorAdd(
    monitorOsmAdd,
    monitorUpdateAddOsmNow,
    monitorGpxAdd,
    monitorUpdateAddMultiGpx,
    monitorUpdateCommon,
    monitorRouteRepository
  )

  val monitorUpdateAnalysis = new MonitorUpdateAnalysis(
    routeRepository,
    monitorRouteRepository,
    monitorRouteDeviationAnalyzer,
    monitorStateStore
  )

  val monitorRouteUpdateExecutor = new MonitorRouteUpdateExecutor(
    monitorUpdateAdd,
    monitorUpdateUpdate,
    monitorUpdateGpxUpload,
    monitorUpdateGpxDelete
  )
}
