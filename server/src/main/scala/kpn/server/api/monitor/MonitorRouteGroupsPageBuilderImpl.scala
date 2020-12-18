package kpn.server.api.monitor

import kpn.api.common.monitor.RouteGroupDetail
import kpn.api.common.monitor.RouteGroupsPage
import kpn.server.repository.MonitorRouteGroupRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteGroupsPageBuilderImpl(
  monitorRouteGroupRepository: MonitorRouteGroupRepository
) extends MonitorRouteGroupsPageBuilder {

  override def build(): Option[RouteGroupsPage] = {
    val groups = monitorRouteGroupRepository.all(stale = false)
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
