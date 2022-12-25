package kpn.server.api.monitor.route

import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorUpdateSaverImpl(
  monitorRouteRepository: MonitorRouteRepository
) extends MonitorUpdateSaver {

  def save(context: MonitorUpdateContext): MonitorUpdateContext = {

    context.newRoute match {
      case Some(route) => monitorRouteRepository.saveRoute(route)
      case None =>
    }

    context.newReferences.foreach { routeRelationReference =>
      monitorRouteRepository.saveRouteRelationReference(routeRelationReference)
    }

    context.newStates.foreach { state =>
      monitorRouteRepository.saveRouteRelationState(state)
    }

    context
  }
}
