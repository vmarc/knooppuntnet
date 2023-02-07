package kpn.server.monitor.group

import kpn.api.common.monitor.MonitorGroupsPage
import kpn.api.common.monitor.MonitorGroupsPageGroup
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorGroupsPageBuilder(
  monitorRepository: MonitorRepository,
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository
) {

  def build(user: Option[String]): Option[MonitorGroupsPage] = {
    val admin = monitorRepository.isAdminUser(user)
    val groups = monitorGroupRepository.groups().sortBy(_.name)
    val groupRouteCounts = monitorRouteRepository.groupRouteCounts().map(grc => grc.groupId.oid -> grc.routeCount).toMap
    val routeCount = groupRouteCounts.values.sum
    Some(
      MonitorGroupsPage(
        admin,
        routeCount,
        groups.map { group =>
          MonitorGroupsPageGroup(
            group._id.oid,
            group.name,
            group.description,
            groupRouteCounts.getOrElse(group._id.oid, 0L)
          )
        }
      )
    )
  }
}
