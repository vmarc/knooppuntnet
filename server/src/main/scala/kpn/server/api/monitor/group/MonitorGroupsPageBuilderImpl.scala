package kpn.server.api.monitor.group

import kpn.api.common.monitor.RouteGroupDetail
import kpn.api.common.monitor.RouteGroupsPage
import kpn.server.repository.MonitorGroupRepository
import org.springframework.stereotype.Component

@Component
class MonitorGroupsPageBuilderImpl(
  monitorGroupRepository: MonitorGroupRepository
) extends MonitorGroupsPageBuilder {

  override def build(): Option[RouteGroupsPage] = {
    val groups = monitorGroupRepository.groups()
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
