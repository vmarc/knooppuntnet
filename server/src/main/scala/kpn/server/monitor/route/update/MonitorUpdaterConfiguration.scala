package kpn.server.monitor.route.update

import kpn.database.base.Database
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzerImpl
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzerImpl
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

  val monitorStore = new MonitorStore(monitorRouteRepository)

  private val monitorUpdateStructure = new MonitorUpdateStructureImpl(
    monitorRouteRelationRepository,
    monitorRouteStructureLoader
  )
  private val monitorRouteOsmSegmentAnalyzer = new MonitorRouteOsmSegmentAnalyzerImpl()
  private val monitorRouteDeviationAnalyzer = new MonitorRouteDeviationAnalyzerImpl()
  private val monitorRouteGapAnalyzer = new MonitorRouteGapAnalyzer()

  private val monitorUpdateCommon = new MonitorUpdateCommon(
    monitorStore,
    routeRepository,
    monitorGroupRepository,
    monitorRouteRepository
  )

  private val monitorUpdateSave = new MonitorUpdateSave(
    routeRepository,
    monitorRouteRepository,
    monitorRouteGapAnalyzer
  )

  private val monitorUpdateAnalyzeReference = new MonitorUpdateAnalyzeReference(
    routeRepository,
    monitorRouteRelationRepository,
    monitorRouteOsmSegmentAnalyzer,
    monitorRouteDeviationAnalyzer
  )

  private val monitorUpdateGpxUpload = new MonitorGpxUpload(
    monitorStore,
    routeRepository,
    monitorRouteRepository,
    monitorUpdateCommon,
    monitorRouteDeviationAnalyzer
  )

  private val monitorUpdateGpxDelete = new MonitorGpxDelete(
    monitorStore,
    monitorRouteRepository,
    monitorUpdateCommon,
    monitorUpdateSave
  )

  private val monitorOsmAnalyze = new MonitorOsmAnalyze(
    monitorStore,
    routeRepository,
    monitorRouteRepository,
    monitorRouteStructureLoader,
    monitorRouteRelationRepository,
    monitorUpdateCommon,
    monitorRouteOsmSegmentAnalyzer,
    monitorRouteDeviationAnalyzer
  )

  private val monitorOsmAdd = new MonitorOsmAdd(
    monitorRouteRepository,
    monitorOsmAnalyze
  )

  private val monitorOsmUpdate = new MonitorOsmUpdate(
    monitorRouteRepository,
    monitorOsmAnalyze
  )

  private val monitorGpxAnalyze = new MonitorGpxAnalyze(monitorStore, routeRepository, monitorRouteRepository, monitorUpdateCommon, monitorOsmAnalyze, monitorRouteDeviationAnalyzer)

  private val monitorGpxUpdate = new MonitorGpxUpdate(
    monitorStore,
    routeRepository,
    monitorGroupRepository,
    monitorRouteRepository,
    monitorUpdateStructure,
    monitorRouteRelationRepository,
    monitorRouteOsmSegmentAnalyzer,
    monitorUpdateAnalyzeReference,
    monitorUpdateCommon,
    monitorUpdateSave,
    monitorGpxAnalyze,
    monitorRouteDeviationAnalyzer
  )

  private val monitorOsmNowAnalyze = new MonitorOsmNowAnalyze(
    monitorStore,
    routeRepository,
    monitorRouteRepository,
    monitorUpdateCommon
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
    monitorStore,
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
    monitorStore,
    routeRepository,
    monitorRouteRepository,
    monitorRouteDeviationAnalyzer
  )

  val monitorRouteUpdateExecutor = new MonitorRouteUpdateExecutor(
    monitorUpdateAdd,
    monitorUpdateUpdate,
    monitorUpdateGpxUpload,
    monitorUpdateGpxDelete
  )
}
