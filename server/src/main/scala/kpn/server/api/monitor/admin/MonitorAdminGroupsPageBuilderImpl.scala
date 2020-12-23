package kpn.server.api.monitor.admin

import kpn.api.common.monitor.MonitorGroupDetail
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.server.repository.MonitorAdminGroupRepository
import org.springframework.stereotype.Component

@Component
class MonitorAdminGroupsPageBuilderImpl(
  monitorAdminGroupRepository: MonitorAdminGroupRepository
) extends MonitorAdminGroupsPageBuilder {

  override def build(): Option[MonitorGroupsPage] = {
    val groups = monitorAdminGroupRepository.groups()
    if (groups.nonEmpty) {
      Some(
        MonitorGroupsPage(
          groups.map { group =>
            MonitorGroupDetail(
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
