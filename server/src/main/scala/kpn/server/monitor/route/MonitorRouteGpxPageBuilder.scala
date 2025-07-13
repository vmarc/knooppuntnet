package kpn.server.monitor.route

import kpn.api.common.monitor.MonitorRouteGpxPage
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteGpxPageBuilder(
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository
) {

  def build(groupName: String, routeName: String, subRelationId: Long): Option[MonitorRouteGpxPage] = {
    monitorGroupRepository.groupByName(groupName).flatMap { group =>
      monitorRouteRepository.routeByName(group._id, routeName).flatMap { route =>
        monitorRouteRepository.reference(route._id, Some(subRelationId)).map { reference =>
          MonitorRouteGpxPage(
            group.name,
            route.name,
            subRelationId,
            "TODO",
            reference.referenceTimestamp,
            reference.referenceFilename,
            reference.referenceDistance
          )
        }
      }
    }
  }
}
