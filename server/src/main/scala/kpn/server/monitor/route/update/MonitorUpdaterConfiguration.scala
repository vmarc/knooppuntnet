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
  private val monitorRouteRelationAnalyzer = new MonitorRouteRelationAnalyzerImpl(
    monitorRouteRelationRepository,
    monitorRouteOsmSegmentAnalyzer,
    monitorRouteDeviationAnalyzer
  )
  private val monitorRouteGapAnalyzer = new MonitorRouteGapAnalyzer()

  val monitorRouteUpdateExecutor = new MonitorRouteUpdateExecutor(
    monitorGroupRepository,
    monitorRouteRepository,
    monitorUpdateStructure,
    monitorRouteRelationAnalyzer,
    monitorRouteRelationRepository,
    monitorRouteOsmSegmentAnalyzer,
    monitorRouteGapAnalyzer
  )
}
