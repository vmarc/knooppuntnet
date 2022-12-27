package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.core.util.Util
import kpn.server.analyzer.engine.monitor.MonitorFilter
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteAnalysis
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteRelationState
import org.springframework.stereotype.Component

@Component
class XxxImpl(
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer,
  monitorRouteDeviationAnalyzer: MonitorRouteDeviationAnalyzer
) {

  def analyzeReference(routeId: ObjectId, reference: MonitorRouteReference): Option[MonitorRouteRelationState] = {

    monitorRouteRelationRepository.loadTopLevel(None, reference.relationId.get) match {
      case None => None
      case Some(relation) =>

        val wayMembers = MonitorFilter.filterWayMembers(relation.wayMembers)
        val osmSegmentAnalysis = monitorRouteOsmSegmentAnalyzer.analyze(wayMembers)
        val deviationAnalysis = monitorRouteDeviationAnalyzer.analyze(wayMembers.map(_.way), reference.geometry)

        val bounds = Util.mergeBounds(osmSegmentAnalysis.routeSegments.map(_.segment.bounds) ++ deviationAnalysis.deviations.map(_.bounds))

        val routeAnalysis = MonitorRouteAnalysis(
          relation,
          wayMembers.size,
          osmSegmentAnalysis.osmDistance,
          deviationAnalysis.referenceDistance,
          bounds,
          osmSegmentAnalysis.routeSegments.map(_.segment),
          Some(deviationAnalysis.referenceGeometry),
          deviationAnalysis.matchesGeometry,
          deviationAnalysis.deviations,
          relations = Seq.empty
        )

        val happy = routeAnalysis.gpxDistance > 0 &&
          routeAnalysis.deviations.isEmpty &&
          routeAnalysis.osmSegments.size == 1

        Some(
          MonitorRouteRelationState(
            ObjectId(),
            routeId,
            reference.relationId.get,
            routeAnalysis.wayCount,
            routeAnalysis.osmDistance,
            routeAnalysis.bounds,
            Some(reference._id),
            routeAnalysis.osmSegments,
            routeAnalysis.matchesGeometry,
            routeAnalysis.deviations,
            happy
          )
        )
    }
  }
}
