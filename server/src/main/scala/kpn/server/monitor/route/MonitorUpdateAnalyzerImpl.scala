package kpn.server.monitor.route

import org.springframework.stereotype.Component

@Component
class MonitorUpdateAnalyzerImpl(
  monitorRouteRelationAnalyzer: MonitorRouteRelationAnalyzer
) extends MonitorUpdateAnalyzer {

  def analyze(context: MonitorUpdateContext): MonitorUpdateContext = {

    val states1 = context.oldReferences.flatMap { reference =>
      monitorRouteRelationAnalyzer.analyzeReference(context.routeId, reference)
    }

    val states2 = context.newReferences.flatMap { reference =>
      monitorRouteRelationAnalyzer.analyzeReference(context.routeId, reference)
    }

    val states = states1 ++ states2

    context.copy(
      newStates = states,
      saveResult = context.saveResult.copy(
        analyzed = states.nonEmpty
      )
    )
  }
}
