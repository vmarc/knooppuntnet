package kpn.server.monitor.route

import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteDetailsPageBuilder(
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository
) {

  def build(groupName: String, routeName: String): Option[MonitorRouteDetailsPage] = {
    monitorGroupRepository.groupByName(groupName).flatMap { group =>
      monitorRouteRepository.routeByName(group._id, routeName).map { route =>
        MonitorRouteDetailsPage(
          group.name,
          group.description,
          route.name,
          route.description,
          route.relationId,
          route.comment,
          route.referenceType,
          route.referenceDay,
          route.referenceFilename,
          route.referenceDistance,
          route.deviationDistance,
          route.deviationCount,
          route.osmSegmentCount,
          route.happy,
          route.osmWayCount,
          route.osmDistance,
          route.relation
        )
      }
    }
  }
}
