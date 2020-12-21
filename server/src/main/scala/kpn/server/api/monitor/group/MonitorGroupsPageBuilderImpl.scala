package kpn.server.api.monitor.group

import kpn.api.common.monitor.MonitorGroupDetail
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.server.repository.MonitorGroupRepository
import org.springframework.stereotype.Component

@Component
class MonitorGroupsPageBuilderImpl(
  monitorGroupRepository: MonitorGroupRepository
) extends MonitorGroupsPageBuilder {

  override def build(): Option[MonitorGroupsPage] = {
    val groups = monitorGroupRepository.groups()
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
