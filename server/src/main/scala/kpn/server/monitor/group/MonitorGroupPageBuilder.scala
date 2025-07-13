package kpn.server.monitor.group

import kpn.api.common.monitor.MonitorGroupPage
import kpn.core.util.NaturalSorting
import kpn.server.config.RequestContext
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorUserRepository
import org.springframework.stereotype.Component

@Component
class MonitorGroupPageBuilder(
  monitorUserRepository: MonitorUserRepository,
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository
) {

  def build(groupName: String): Option[MonitorGroupPage] = {
    val admin = monitorUserRepository.isAdminUser(RequestContext.user)
    monitorGroupRepository.groupByName(groupName).map { group =>
      val routeDetails = monitorRouteRepository.groupRouteDetails(group._id)
      val sortedRouteDetails = NaturalSorting.sortBy(routeDetails)(s => s"${s.name}-")
      MonitorGroupPage(
        group._id.oid,
        groupName,
        group.description,
        admin,
        sortedRouteDetails.zipWithIndex.map { case (route, rowIndex) =>
          route.copy(rowIndex = rowIndex)
        }
      )
    }
  }
}
