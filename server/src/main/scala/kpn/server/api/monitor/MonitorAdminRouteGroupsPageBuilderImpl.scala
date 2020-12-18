package kpn.server.api.monitor

import kpn.api.common.monitor.RouteGroupDetail
import kpn.api.common.monitor.RouteGroupsPage
import kpn.server.repository.MonitorAdminRouteGroupRepository
import org.springframework.stereotype.Component

@Component
class MonitorAdminRouteGroupsPageBuilderImpl(
  monitorAdminRouteGroupRepository: MonitorAdminRouteGroupRepository
) extends MonitorAdminRouteGroupsPageBuilder {

  override def build(): Option[RouteGroupsPage] = {
    val groups = monitorAdminRouteGroupRepository.all(stale = false)
    if (groups.nonEmpty) {
      Some(
        RouteGroupsPage(
          groups.map { group =>
            RouteGroupDetail(
              group.name,
              group.description
            )
          }
        )
      )
    }
    else {
      None
    }
  }
}
