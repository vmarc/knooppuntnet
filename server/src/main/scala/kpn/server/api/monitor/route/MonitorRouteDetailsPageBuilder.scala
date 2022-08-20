package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteDetailsPageBuilder(
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository
) {

  def build(groupName: String, routeName: String): Option[MonitorRouteDetailsPage] = {
    monitorGroupRepository.groupByName(groupName).flatMap { group =>
      monitorRouteRepository.routeByName(group._id, routeName).map { route =>
        val routeStateOption = monitorRouteRepository.routeState(route._id)
        MonitorRouteDetailsPage(
          route._id.oid,
          group.name,
          group.description,
          route.name,
          route.description,
          route.relationId,
          routeStateOption.map(_.wayCount).getOrElse(0L),
          routeStateOption.map(_.osmDistance).getOrElse(0L),
          routeStateOption.map(_.gpxDistance).getOrElse(0L),
          None, // TODO routeState.gpxFilename,
          happy = routeStateOption.exists(_.happy),
          routeStateOption.map(_.osmSegments.size.toLong).getOrElse(0L),
          routeStateOption.map(_.nokSegments.size.toLong).getOrElse(0L)
        )
      }
    }
  }
}
