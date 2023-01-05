package kpn.server.api.monitor.route

import kpn.database.base.Database
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzerImpl
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzerImpl
import kpn.server.repository.MonitorGroupRepositoryImpl
import kpn.server.repository.MonitorRouteRepositoryImpl

class MonitorUpdaterConfiguration(database: Database, val monitorRouteRelationRepository: MonitorRouteRelationRepository) {

  val monitorGroupRepository = new MonitorGroupRepositoryImpl(database)
  val monitorRouteRepository = new MonitorRouteRepositoryImpl(database)
  private val monitorUpdateRoute = new MonitorUpdateRouteImpl(monitorGroupRepository)
  private val monitorUpdateStructure = new MonitorUpdateStructureImpl(monitorRouteRelationRepository)
  private val monitorRouteOsmSegmentAnalyzer = new MonitorRouteOsmSegmentAnalyzerImpl()
  private val monitorRouteDeviationAnalyzer = new MonitorRouteDeviationAnalyzerImpl()
  private val monitorUpdateReference = new MonitorUpdateReferenceImpl(monitorRouteRelationRepository, monitorRouteOsmSegmentAnalyzer)
  private val monitorRouteRelationAnalyzer = new MonitorRouteRelationAnalyzerImpl(
    monitorRouteRelationRepository,
    monitorRouteOsmSegmentAnalyzer,
    monitorRouteDeviationAnalyzer
  )

  private val monitorUpdateAnalyzer = new MonitorUpdateAnalyzerImpl(
    monitorRouteRelationAnalyzer
  )
  private val saver = new MonitorUpdateSaverImpl(monitorRouteRepository)
  val monitorUpdater = new MonitorUpdaterImpl(
    monitorGroupRepository,
    monitorRouteRepository,
    monitorUpdateRoute,
    monitorUpdateStructure,
    monitorUpdateReference,
    monitorUpdateAnalyzer,
    saver,
    monitorRouteRelationAnalyzer
  )
}
