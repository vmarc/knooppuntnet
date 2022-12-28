package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState
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

    context.copy(
      newStates = states,
    )
  }

  private def analyzeReference(context: MonitorUpdateContext, reference: MonitorRouteReference): Option[MonitorRouteState] = {
    xxx.analyzeReference(context.routeId, reference)
  }

  private def updateMonitorRouteRelation(states: Seq[MonitorRouteState], monitorRouteRelation: MonitorRouteRelation): MonitorRouteRelation = {

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
