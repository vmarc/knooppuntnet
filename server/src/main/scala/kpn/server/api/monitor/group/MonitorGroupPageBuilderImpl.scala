package kpn.server.api.monitor.group

import kpn.api.common.monitor.MonitorGroupPage
import kpn.api.common.monitor.MonitorRouteDetail
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRepository
import org.springframework.stereotype.Component

@Component
class MonitorGroupPageBuilderImpl(
  monitorRepository: MonitorRepository,
  monitorGroupRepository: MonitorGroupRepository
) extends MonitorGroupPageBuilder {

  override def build(user: Option[String], groupName: String): Option[MonitorGroupPage] = {
    val admin = monitorRepository.isAdminUser(user)
    monitorGroupRepository.group(groupName).map { group =>
      val routes = monitorGroupRepository.groupRoutes(groupName)
      MonitorGroupPage(
        admin,
        groupName,
        group.description,
        routes.map { route =>
          MonitorRouteDetail(
            route._id,
            route.routeId,
            route.name,
            route.description,
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
