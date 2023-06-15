package kpn.server.monitor.route

import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository

class MonitorRouteUpdateConfiguration(
  val monitorGroupRepository: MonitorGroupRepository,
  val monitorRouteRepository: MonitorRouteRepository,
  val monitorUpdateRoute: MonitorUpdateRoute,
  val monitorUpdateStructure: MonitorUpdateStructure,
  val saver: MonitorUpdateSaver,
  val monitorRouteRelationAnalyzer: MonitorRouteRelationAnalyzer,
  val monitorRouteRelationRepository: MonitorRouteRelationRepository,
  val monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer
)
