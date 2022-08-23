package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorRouteGroup
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteUpdatePage
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteUpdatePageBuilder(
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository
) {

  def build(groupName: String, routeName: String): Option[MonitorRouteUpdatePage] = {
    monitorGroupRepository.groupByName(groupName) match {
      case None => throw new IllegalArgumentException(s"""Could not find group "$groupName"""")
      case Some(group) =>
        monitorRouteRepository.routeByName(group._id, routeName) match {
          case None => throw new IllegalArgumentException(s"""Could not find route "$routeName" in group "$groupName"""")
          case Some(route) =>
            val groups = monitorGroupRepository.groups().map { group =>
              MonitorRouteGroup(
                group.name,
                group.description
              )
            }
            monitorRouteRepository.currentRouteReference(route._id) match { // TODO MON performance: only pick up relevant reference info (exclude geometry)
              case None => throw new IllegalArgumentException(s"""Could not find reference for route "$routeName" in group "$groupName"""")
              case Some(reference) =>
                Some(
                  MonitorRouteUpdatePage(
                    groupName = group.name,
                    groupDescription = group.description,
                    routeName = route.name,
                    routeDescription = route.description,
                    groups = groups,
                    properties = MonitorRouteProperties(
                      name = route.name,
                      description = route.description,
                      relationId = route.relationId.map(_.toString),
                      referenceType = reference.referenceType,
                      referenceTimestamp = None, // TODO MON reference.referenceTimestamp,
                      gpxFilename = reference.filename
                    )
                  )
                )
            }
        }
    }
  }
}
