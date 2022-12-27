package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteRelationState
import org.springframework.stereotype.Component

@Component
class MonitorUpdateAnalyzerImpl(
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer,
  monitorRouteDeviationAnalyzer: MonitorRouteDeviationAnalyzer,
  xxx: XxxImpl,
) extends MonitorUpdateAnalyzer {

  def analyze(context: MonitorUpdateContext): MonitorUpdateContext = {
    val states = context.newReferences.flatMap { reference: MonitorRouteReference =>
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

  private def analyzeReference(context: MonitorUpdateContext, reference: MonitorRouteReference): Option[MonitorRouteRelationState] = {
    xxx.analyzeReference(context.routeId, reference)
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
