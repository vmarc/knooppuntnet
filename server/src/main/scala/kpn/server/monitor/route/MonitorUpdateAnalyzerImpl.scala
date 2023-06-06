package kpn.server.monitor.route

import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class MonitorUpdateAnalyzerImpl(
  monitorRouteRelationAnalyzer: MonitorRouteRelationAnalyzer
) extends MonitorUpdateAnalyzer {

  private val log = Log(classOf[MonitorUpdateAnalyzerImpl])

  def analyze(context: MonitorUpdateContext): MonitorUpdateContext = {

    val states1 = context.oldReferences.flatMap { reference =>
      log.info(s"analyze old reference ${reference.relationId}")
      monitorRouteRelationAnalyzer.analyzeReference(context.routeId, reference)
    }

    val states2 = context.newReferences.flatMap { reference =>
      log.info(s"analyze new reference ${reference.relationId}")
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
