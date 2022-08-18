package kpn.server.api.monitor.group

import kpn.api.common.monitor.MonitorGroupsPage
import kpn.api.common.monitor.MonitorGroupsPageGroup
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRepository
import org.springframework.stereotype.Component

@Component
class MonitorGroupsPageBuilderImpl(
  monitorRepository: MonitorRepository,
  monitorGroupRepository: MonitorGroupRepository
) extends MonitorGroupsPageBuilder {

  override def build(user: Option[String]): Option[MonitorGroupsPage] = {
    val admin = monitorRepository.isAdminUser(user)
    val groups = monitorGroupRepository.groups()
    Some(
      MonitorGroupsPage(
        admin,
        groups.map { group =>
          MonitorGroupsPageGroup(
            group._id.oid,
            group.name,
            group.description
          )
        }
      )
    )
  }
}
