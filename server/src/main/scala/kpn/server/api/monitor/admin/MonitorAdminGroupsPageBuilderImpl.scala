package kpn.server.api.monitor.admin

import kpn.api.common.monitor.MonitorAdminGroupDetail
import kpn.api.common.monitor.MonitorAdminGroupsPage
import kpn.api.common.monitor.RouteGroupDetail
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.server.repository.MonitorAdminGroupRepository
import org.springframework.stereotype.Component

@Component
class MonitorAdminGroupsPageBuilderImpl(
  monitorAdminGroupRepository: MonitorAdminGroupRepository
) extends MonitorAdminGroupsPageBuilder {

  override def build(): Option[MonitorAdminGroupsPage] = {
    val groups = monitorAdminGroupRepository.groups()
    if (groups.nonEmpty) {
      Some(
        MonitorAdminGroupsPage(
          groups.map { group =>
            MonitorAdminGroupDetail(
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
