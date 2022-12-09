package kpn.server.analyzer.engine.monitor

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.custom.Relation
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteState
import kpn.server.repository.MonitorRouteRepository

class MonitorRouteStateUpdater(routeRepository: MonitorRouteRepository) {

  def update(route: MonitorRoute, newState: MonitorRouteState, routeRelation: Relation): Unit = {

    val state = routeRepository.routeState(route._id) match {
      case Some(previousState) => newState.copy(_id = previousState._id)
      case None => newState
    }
    routeRepository.saveRouteState(state)

    val deviationDistance = Math.round(state.deviations.map(_.distance).sum.toFloat / 1000)
    val deviationCount = state.deviations.size
    val osmSegmentCount = state.osmSegments.size
    val happy = route.referenceDistance > 0 && deviationCount == 0 && osmSegmentCount == 1
    val relation = MonitorRouteRelation.from(routeRelation, None)

    val newRoute = route.copy(
      deviationDistance = deviationDistance,
      deviationCount = deviationCount,
      osmWayCount = newState.wayCount,
      osmDistance = newState.osmDistance,
      osmSegmentCount = osmSegmentCount,
      happy = happy,
      relation = Some(relation)
    )
    routeRepository.saveRoute(newRoute)
  }
}
