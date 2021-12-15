package kpn.server.api.monitor.group

import kpn.api.common.monitor.MonitorGroupPage
import kpn.api.common.monitor.MonitorRouteDetail
import kpn.server.repository.MonitorGroupRepository
import org.springframework.stereotype.Component

@Component
class MonitorGroupPageBuilderImpl(
  monitorGroupRepository: MonitorGroupRepository
) extends MonitorGroupPageBuilder {

  override def build(groupName: String): Option[MonitorGroupPage] = {

    monitorGroupRepository.group(groupName).map { group =>

      val routes = monitorGroupRepository.groupRoutes(groupName)
      MonitorGroupPage(
        groupName,
        group.description,
        routes.map { route =>
          MonitorRouteDetail(
            route._id,
            route.routeId,
            route.ref,
            route.routeName,
            route.description,
            route.operator,
            route.website,
            wayCount = 0,
            osmDistance = 0,
            gpxDistance = 0,
            gpxFilename = None,
            osmHappy = false,
            gpxHappy = false
          )
        }
      )
    }
  }
}
