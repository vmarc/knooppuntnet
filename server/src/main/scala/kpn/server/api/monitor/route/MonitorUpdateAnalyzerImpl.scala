package kpn.server.api.monitor.route

import kpn.server.api.monitor.domain.MonitorRouteReference
import org.springframework.stereotype.Component

@Component
class MonitorUpdateAnalyzerImpl(
  monitorRouteRelationAnalyzer: MonitorRouteRelationAnalyzer
) extends MonitorUpdateAnalyzer {

  def analyze(context: MonitorUpdateContext): MonitorUpdateContext = {
    val states = context.newReferences.flatMap { reference: MonitorRouteReference =>
      monitorRouteRelationAnalyzer.analyzeReference(context.routeId, reference)
    }

    context.copy(
      newStates = states,
      saveResult = context.saveResult.copy(
        analyzed = states.nonEmpty
      )
    )
  }
}
