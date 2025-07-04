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

  private val monitorUpdateMultiGpxUpload = new MonitorMultiGpxUpload(
    routeRepository,
    monitorRouteRepository,
    monitorUpdateCommon,
    monitorRouteDeviationAnalyzer
  )

  private val monitorUpdateGpxUpload = new MonitorGpxUpload(
    monitorRouteRepository,
    monitorRouteRelationRepository,
    monitorUpdateAnalyzeReference,
    monitorUpdateCommon,
    monitorUpdateSave,
    monitorUpdateMultiGpxUpload
  )

  private val monitorUpdateGpxDelete = new MonitorGpxDelete(
    monitorRouteRepository,
    monitorUpdateCommon,
    monitorUpdateSave
  )

  private val monitorUpdateUpdate = new MonitorUpdate(
    monitorGroupRepository,
    monitorRouteRepository,
    monitorUpdateStructure,
    monitorRouteRelationRepository,
    monitorRouteOsmSegmentAnalyzer,
    monitorUpdateAnalyzeReference,
    monitorUpdateCommon,
    monitorUpdateSave
  )

  private val monitorUpdateAddMultiGpx = new MonitorAddMultigpx(
    routeRepository,
    monitorRouteRepository,
    monitorUpdateCommon,
  )

  private val monitorUpdateAdd = new MonitorAdd(
    monitorRouteRepository,
    monitorUpdateStructure,
    monitorUpdateUpdate,
    monitorUpdateCommon,
    monitorUpdateSave,
    monitorUpdateAddMultiGpx
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
