package kpn.server.analyzer.engine.monitor

import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState
import kpn.server.repository.MonitorRouteRepository

class MonitorRouteStateUpdater(routeRepository: MonitorRouteRepository) {

  def update(route: MonitorRoute, newState: MonitorRouteState, routeReference: MonitorRouteReference): Unit = {

    val state = routeRepository.routeState(route._id) match {
      case Some(previousState) => newState.copy(_id = previousState._id)
      case None => newState
    }
    routeRepository.saveRouteState(state)

    val referenceDistance = state.gpxDistance // km
    val deviationDistance = Math.round(state.deviations.map(_.distance).sum.toFloat / 1000)
    val deviationCount = state.deviations.size
    val osmSegmentCount = state.osmSegments.size
    val happy = referenceDistance > 0 && deviationCount == 0 && osmSegmentCount == 1

    val newRoute = route.copy(
      referenceType = Some(routeReference.referenceType),
      referenceDay = routeReference.referenceDay,
      referenceFilename = routeReference.filename,
      referenceDistance = referenceDistance,
      deviationDistance = deviationDistance,
      deviationCount = deviationCount,
      osmWayCount = newState.wayCount,
      osmDistance = newState.osmDistance,
      osmSegmentCount = osmSegmentCount,
      happy = happy
    )
    routeRepository.saveRoute(newRoute)
  }
}
