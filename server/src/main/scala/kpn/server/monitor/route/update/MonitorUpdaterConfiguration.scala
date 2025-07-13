package kpn.server.monitor.route.update

import kpn.database.base.Database
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzerImpl
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzerImpl
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculatorImpl
import kpn.server.analyzer.engine.tile.RouteTileCache
import kpn.server.monitor.repository.MonitorGroupRepositoryImpl
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

class MonitorUpdaterConfiguration(
  database: Database,
  val monitorRouteRelationRepository: MonitorRouteRelationRepository,
  val monitorRouteStructureLoader: MonitorRouteStructureLoader
) {
  val routeRepository = new RouteRepositoryImpl(database)

  val monitorGroupRepository = new MonitorGroupRepositoryImpl(database)
  val monitorRouteRepository = new MonitorRouteRepositoryImpl(database)

  private val routeTileCache = new RouteTileCache()
  private val lineSegmentTileCalculator = new LineSegmentTileCalculatorImpl(routeTileCache)

  private val monitorRouteOsmSegmentAnalyzer = new MonitorRouteOsmSegmentAnalyzerImpl()
  private val monitorRouteDeviationAnalyzer = new MonitorRouteDeviationAnalyzerImpl()
  private val monitorRouteGapAnalyzer = new MonitorRouteGapAnalyzer()

  private val monitorUpdateCommon = new MonitorUpdateCommon(
    routeRepository,
    monitorGroupRepository,
    monitorRouteRepository
  )

  private val monitorReferenceBuilder = new MonitorReferenceBuilder(lineSegmentTileCalculator)

  private val monitorStateBuilder = new MonitorStateBuilder(lineSegmentTileCalculator)

  private val monitorUpdateAnalyzeReference = new MonitorUpdateAnalyzeReference(
    routeRepository,
    monitorRouteRelationRepository,
    monitorRouteOsmSegmentAnalyzer,
    monitorRouteDeviationAnalyzer,
    monitorStateBuilder
  )

  private val monitorUpdateGpxUpload = new MonitorGpxUpload(
    routeRepository,
    monitorRouteRepository,
    monitorUpdateCommon,
    monitorRouteDeviationAnalyzer,
    monitorReferenceBuilder,
    monitorStateBuilder
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
    monitorStateBuilder
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
    monitorStateBuilder
  )

  private val monitorGpxUpdate = new MonitorGpxUpdate(
    routeRepository,
    monitorRouteRepository,
    monitorGpxAnalyze,
    monitorRouteDeviationAnalyzer,
    monitorReferenceBuilder,
    monitorStateBuilder
  )

  private val monitorOsmNowAnalyze = new MonitorOsmNowAnalyze(
    routeRepository,
    monitorRouteRepository,
    monitorUpdateCommon,
    monitorReferenceBuilder,
    monitorStateBuilder
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
    monitorMultiGpxUpdate
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
    monitorStateBuilder
  )

  val monitorRouteUpdateExecutor = new MonitorRouteUpdateExecutor(
    monitorUpdateAdd,
    monitorUpdateUpdate,
    monitorUpdateGpxUpload,
    monitorUpdateGpxDelete
  )
}
