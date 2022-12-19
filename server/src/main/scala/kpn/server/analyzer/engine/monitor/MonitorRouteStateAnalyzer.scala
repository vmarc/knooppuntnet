package kpn.server.analyzer.engine.monitor

import kpn.api.base.ObjectId
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.common.RelationUtil
import kpn.core.util.Util
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteAnalysis
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState

class MonitorRouteStateAnalyzer() {

  def analyze(
    route: MonitorRoute,
    routeReference: MonitorRouteReference,
    routeRootRelation: Relation,
    now: Timestamp
  ): MonitorRouteState = {

    val allRelations = RelationUtil.relationsInRelation(routeRootRelation)
    val allWayMembers = allRelations.flatMap(relation => relation.wayMembers)
    val wayMembers = MonitorRouteWayFilter.filter(allWayMembers)

    val osmSegmentAnalysis = new MonitorRouteOsmSegmentAnalyzer().analyze(wayMembers)
    val deviationAnalysis = new MonitorRouteDeviationAnalyzer().analyze(wayMembers.map(_.way), routeReference)

    // TODO merge gpx bounds + ok
    val bounds = Util.mergeBounds(osmSegmentAnalysis.routeSegments.map(_.segment.bounds) ++ deviationAnalysis.deviations.map(_.bounds))

    val routeAnalysis = MonitorRouteAnalysis(
      routeRootRelation,
      wayMembers.size,
      osmSegmentAnalysis.osmDistance,
      deviationAnalysis.referenceDistance,
      bounds,
      osmSegmentAnalysis.routeSegments.map(_.segment),
      Some(deviationAnalysis.referenceGeometry),
      deviationAnalysis.matchesGeometry,
      deviationAnalysis.deviations,
      relations = Seq.empty // TODO add analysis result per sub-relation where applicable
    )

    val happy = routeAnalysis.gpxDistance > 0 &&
      routeAnalysis.deviations.isEmpty &&
      routeAnalysis.osmSegments.size == 1

    MonitorRouteState(
      ObjectId(),
      route._id,
      now,
      routeAnalysis.wayCount,
      routeAnalysis.osmDistance,
      routeAnalysis.gpxDistance,
      routeAnalysis.bounds,
      Some(routeReference._id),
      routeAnalysis.osmSegments,
      routeAnalysis.matchesGeometry,
      routeAnalysis.deviations,
      happy,
      Seq.empty // TODO add state per sub-relation where applicable
    )
  }
}
