package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.core.util.Util
import kpn.server.analyzer.engine.monitor.MonitorFilter
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteAnalysis
import kpn.server.api.monitor.domain.MonitorRouteRelationReference
import kpn.server.api.monitor.domain.MonitorRouteRelationState
import org.springframework.stereotype.Component

@Component
class MonitorUpdateAnalyzerImpl(
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer,
  monitorRouteDeviationAnalyzer: MonitorRouteDeviationAnalyzer
) extends MonitorUpdateAnalyzer {

  def analyze(context: MonitorUpdateContext): MonitorUpdateContext = {
    val states = context.newReferences.flatMap { reference: MonitorRouteRelationReference =>
      analyzeReference(context, reference)
    }

    val deviationDistance = states.flatMap(_.deviations.map(_.distance)).sum
    val deviationCount = states.map(_.deviations.size).sum
    val osmWayCount = states.map(_.wayCount).sum
    val osmDistance = states.map(_.osmDistance).sum
    val osmSegmentCount = states.map(_.osmSegments.size).sum // TODO should merge osmsegments first !!!
    val happy = osmSegmentCount == 1 && deviationCount == 0

    val updatedNewRoute = context.newRoute.map { newRoute =>
      val updatedMonitorRouteRelation = newRoute.relation match {
        case None => None
        case Some(monitorRouteRelation) =>
          val relations = monitorRouteRelation.relations.map(xx => updateMonitorRouteRelation(states, xx))
          Some(
            monitorRouteRelation.copy(
              deviationDistance = deviationDistance,
              deviationCount = deviationCount,
              osmWayCount = osmWayCount,
              osmDistance = osmDistance,
              osmSegmentCount = osmSegmentCount,
              happy = happy,
              relations = relations
            )
          )
      }

      newRoute.copy(
        deviationDistance = deviationDistance,
        deviationCount = deviationCount,
        osmWayCount = osmWayCount,
        osmDistance = osmDistance,
        osmSegmentCount = osmSegmentCount,
        happy = happy,
        relation = updatedMonitorRouteRelation
      )
    }

    context.copy(
      newRoute = updatedNewRoute,
      newStates = states,
    )
  }

  private def analyzeReference(context: MonitorUpdateContext, reference: MonitorRouteRelationReference): Option[MonitorRouteRelationState] = {

    monitorRouteRelationRepository.loadTopLevel(None, reference.relationId) match {
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
            context.routeId,
            reference.relationId,
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

  private def updateMonitorRouteRelation(states: Seq[MonitorRouteRelationState], monitorRouteRelation: MonitorRouteRelation): MonitorRouteRelation = {

    val relations = monitorRouteRelation.relations.map(rel => updateMonitorRouteRelation(states, rel))

    states.find(_.relationId == monitorRouteRelation.relationId) match {
      case None =>
        monitorRouteRelation.copy(
          relations = relations
        )
      case Some(state) =>
        monitorRouteRelation.copy(
          deviationDistance = state.deviations.map(_.distance).sum,
          deviationCount = state.deviations.size,
          osmWayCount = state.wayCount,
          osmDistance = state.osmDistance,
          osmSegmentCount = state.osmSegments.size,
          happy = state.happy,
          relations = relations
        )
    }
  }
}
