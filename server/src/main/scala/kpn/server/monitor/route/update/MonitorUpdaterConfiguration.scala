package kpn.server.monitor.route.update

import kpn.database.base.Database
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzerImpl
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzerImpl
import kpn.server.monitor.repository.MonitorGroupRepositoryImpl
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl

class MonitorUpdaterConfiguration(
  database: Database,
  val monitorRouteRelationRepository: MonitorRouteRelationRepository,
  val monitorRouteStructureLoader: MonitorRouteStructureLoader
) {

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
    monitorRouteRepository,
    monitorRouteGapAnalyzer,
  )

  private val monitorUpdateAnalyzeReference = new MonitorUpdateAnalyzeReference(
    monitorRouteRelationRepository,
    monitorRouteOsmSegmentAnalyzer,
    monitorRouteDeviationAnalyzer,
  )

  private val monitorUpdateGpxUpload = new MonitorUpdateGpxUpload(
    monitorRouteRepository,
    monitorRouteRelationRepository,
    monitorUpdateAnalyzeReference,
    monitorUpdateCommon,
    monitorUpdateSave
  )

  private val monitorUpdateGpxDelete = new MonitorUpdateGpxDelete(
    monitorRouteRepository,
    monitorUpdateCommon,
    monitorUpdateSave
  )

  private val monitorUpdateUpdate = new MonitorUpdateUpdate(
    monitorGroupRepository,
    monitorRouteRepository,
    monitorUpdateStructure,
    monitorRouteRelationRepository,
    monitorRouteOsmSegmentAnalyzer,
    monitorUpdateAnalyzeReference,
    monitorUpdateCommon,
    monitorUpdateSave
  )

  private val monitorUpdateAdd = new MonitorUpdateAdd(
    monitorRouteRepository,
    monitorUpdateStructure,
    monitorRouteRelationRepository,
    monitorRouteOsmSegmentAnalyzer,
    monitorUpdateUpdate,
    monitorUpdateCommon,
    monitorUpdateSave
  )

  val monitorRouteUpdateExecutor = new MonitorRouteUpdateExecutor(
    monitorGroupRepository,
    monitorRouteRepository,
    monitorUpdateStructure,
    monitorRouteRelationRepository,
    monitorRouteOsmSegmentAnalyzer,
    monitorRouteGapAnalyzer,
    monitorRouteDeviationAnalyzer,
    monitorUpdateGpxUpload,
    monitorUpdateGpxDelete,
    monitorUpdateAdd,
    monitorUpdateUpdate,
    monitorUpdateAnalyzeReference,
    monitorUpdateCommon,
    monitorUpdateSave,
  )
}
