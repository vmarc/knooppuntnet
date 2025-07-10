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
  private val monitorUpdateStructure = new MonitorUpdateStructureImpl(
    monitorRouteRelationRepository,
    monitorRouteStructureLoader
  )
  private val monitorRouteOsmSegmentAnalyzer = new MonitorRouteOsmSegmentAnalyzerImpl()
  private val monitorRouteDeviationAnalyzer = new MonitorRouteDeviationAnalyzerImpl()
  private val monitorRouteGapAnalyzer = new MonitorRouteGapAnalyzer()

  private val monitorUpdateCommon = new MonitorUpdateCommon(
    routeRepository,
    monitorGroupRepository,
    monitorRouteRepository,
  )

  private val monitorUpdateSave = new MonitorUpdateSave(
    routeRepository,
    monitorRouteRepository,
    monitorRouteGapAnalyzer,
  )

  private val monitorUpdateAnalyzeReference = new MonitorUpdateAnalyzeReference(
    routeRepository,
    monitorRouteRelationRepository,
    monitorRouteOsmSegmentAnalyzer,
    monitorRouteDeviationAnalyzer,
  )

  private val monitorUpdateGpxUpload = new MonitorGpxUpload(
    routeRepository,
    monitorRouteRepository,
    monitorUpdateCommon,
    monitorRouteDeviationAnalyzer
  )

  private val monitorUpdateGpxDelete = new MonitorGpxDelete(
    monitorRouteRepository,
    monitorUpdateCommon,
    monitorUpdateSave,
  )

  private val monitorOsmAnalyze = new MonitorOsmAnalyze(
    routeRepository,
    monitorRouteRepository,
    monitorRouteStructureLoader,
    monitorRouteRelationRepository,
    monitorUpdateCommon,
    monitorRouteOsmSegmentAnalyzer,
    monitorRouteDeviationAnalyzer,
  )

  private val monitorOsmAdd = new MonitorOsmAdd(
    monitorRouteRepository,
    monitorUpdateCommon,
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
    monitorRouteDeviationAnalyzer
  )

  private val monitorGpxUpdate = new MonitorGpxUpdate(
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
    routeRepository,
    monitorRouteRepository,
    monitorUpdateCommon,
  )

  private val monitorOsmNowUpdate = new MonitorOsmNowUpdate(
    monitorRouteRepository,
    monitorOsmNowAnalyze
  )

  private val monitorMultiGpxUpdate = new MonitorMultiGpxUpdate(
    routeRepository,
    monitorRouteRepository,
    monitorUpdateCommon,
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
    monitorUpdateCommon,
  )

  private val monitorUpdateAddOsmNow = new MonitorOsmNowAdd(
    routeRepository,
    monitorRouteRepository,
    monitorUpdateCommon,
    monitorOsmNowAnalyze
  )

  private val monitorGpxAdd = new MonitorGpxAdd(
    routeRepository,
    monitorRouteRepository,
    monitorUpdateCommon,
    monitorGpxAnalyze,
    monitorRouteDeviationAnalyzer
  )

  private val monitorUpdateAdd = new MonitorAdd(
    monitorUpdateAddMultiGpx,
    monitorUpdateAddOsmNow,
    monitorOsmAdd,
    monitorGpxAdd,
  )

  val monitorUpdateAnalysis = new MonitorUpdateAnalysis(
    monitorRouteRepository,
    monitorUpdateStructure,
    monitorUpdateAnalyzeReference,
    monitorUpdateCommon,
    monitorUpdateSave
  )

  val monitorRouteUpdateExecutor = new MonitorRouteUpdateExecutor(
    monitorUpdateAdd,
    monitorUpdateUpdate,
    monitorUpdateGpxUpload,
    monitorUpdateGpxDelete,
  )
}
