package kpn.server.monitor.route

import kpn.api.base.ObjectId
import kpn.core.common.Time
import kpn.core.util.Util
import kpn.server.analyzer.engine.monitor.MonitorFilter
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteAnalysis
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import org.springframework.stereotype.Component

@Component
class MonitorRouteRelationAnalyzerImpl(
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer,
  monitorRouteDeviationAnalyzer: MonitorRouteDeviationAnalyzer
) extends MonitorRouteRelationAnalyzer {

  override def analyzeReference(routeId: ObjectId, reference: MonitorRouteReference): Option[MonitorRouteState] = {

    reference.relationId.flatMap { relationId =>

      monitorRouteRelationRepository.loadTopLevel(None, relationId) match {
        case None => None
        case Some(relation) =>

          val wayMembers = MonitorFilter.filterWayMembers(relation.wayMembers)
          val osmSegmentAnalysis = monitorRouteOsmSegmentAnalyzer.analyze(wayMembers)
          val deviationAnalysis = monitorRouteDeviationAnalyzer.analyze(wayMembers.map(_.way), reference.geoJson)

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
            MonitorRouteState(
              ObjectId(),
              routeId,
              relationId,
              Time.now,
              routeAnalysis.wayCount,
              routeAnalysis.osmDistance,
              routeAnalysis.bounds,
              routeAnalysis.osmSegments,
              routeAnalysis.matchesGeometry,
              routeAnalysis.deviations,
              happy,
            )
          )
      }
    }
  }
}
