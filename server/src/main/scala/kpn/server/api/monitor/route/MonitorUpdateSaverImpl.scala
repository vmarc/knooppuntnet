package kpn.server.api.monitor.route

import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorUpdateSaverImpl(
  monitorRouteRepository: MonitorRouteRepository
) extends MonitorUpdateSaver {

  def save(originalContext: MonitorUpdateContext): MonitorUpdateContext = {

    var context: MonitorUpdateContext = originalContext

    context.newReferences.foreach { routeRelationReference =>
      monitorRouteRepository.saveRouteReference(routeRelationReference)
    }

    context.newStates.foreach { state =>
      monitorRouteRepository.saveRouteState(state)
    }

    if (context.newReferences.nonEmpty) {
      // TODO this is only needed for routes with multiple references !
      monitorRouteRepository.superRouteReferenceSummary(context.routeId) match {
        case None =>
        case Some(referenceDistance) =>
          context.newRoute match {
            case None =>
              context.oldRoute match {
                case None =>
                case Some(oldRoute) =>
                  val updatedRoute = oldRoute.copy(
                    referenceDistance = referenceDistance
                  )
                  context = context.copy(
                    newRoute = Some(updatedRoute)
                  )
              }

            case Some(newRoute) =>
              val updatedRoute = newRoute.copy(
                referenceDistance = referenceDistance
              )
              context = context.copy(
                newRoute = Some(updatedRoute)
              )
          }
      }
    }

    if (context.newStates.nonEmpty) {
      // TODO this is only needed for routes with multiple states !
      monitorRouteRepository.superRouteStateSummary(context.routeId) match {
        case None =>
        case Some(monitorRouteStateSummary) =>
          context.newRoute match {
            case None =>
              context.oldRoute match {
                case None =>
                case Some(oldRoute) =>
                  val updatedRoute = oldRoute.copy(
                    deviationDistance = monitorRouteStateSummary.deviationDistance,
                    deviationCount = monitorRouteStateSummary.deviationCount,
                    osmWayCount = monitorRouteStateSummary.osmWayCount,
                    osmDistance = monitorRouteStateSummary.osmDistance,
                  )
                  context = context.copy(
                    newRoute = Some(updatedRoute)
                  )
              }

            case Some(newRoute) =>
              val updatedRoute = newRoute.copy(
                deviationDistance = monitorRouteStateSummary.deviationDistance,
                deviationCount = monitorRouteStateSummary.deviationCount,
                osmWayCount = monitorRouteStateSummary.osmWayCount,
                osmDistance = monitorRouteStateSummary.osmDistance,
              )
              context = context.copy(
                newRoute = Some(updatedRoute)
              )
          }
      }
    }

    context.newRoute match {
      case Some(route) => monitorRouteRepository.saveRoute(route)
      case None =>
    }

    context
  }
}
