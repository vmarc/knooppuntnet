package kpn.server.monitor.group

import kpn.api.common.monitor.MonitorGroupPage
import kpn.api.common.monitor.MonitorRouteDetail
import kpn.core.util.NaturalSorting
import kpn.server.config.RequestContext
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRepository
import org.springframework.stereotype.Component

@Component
class MonitorGroupPageBuilder(
  monitorRepository: MonitorRepository,
  monitorGroupRepository: MonitorGroupRepository
) {

  def build(groupName: String): Option[MonitorGroupPage] = {
    val admin = monitorRepository.isAdminUser(RequestContext.user)
    monitorGroupRepository.groupByName(groupName).map { group =>
      val routes = monitorGroupRepository.groupRoutes(group._id)
      val sortedRoutes = NaturalSorting.sortBy(routes)(s => s.name + "-")
      MonitorGroupPage(
        group._id.oid,
        groupName,
        group.description,
        admin,
        sortedRoutes.zipWithIndex.map { case (route, rowIndex) =>
          MonitorRouteDetail(
            rowIndex,
            route._id.oid,
            route.name,
            route.description,
            route.symbol,
            route.relationId,
            route.referenceType,
            route.referenceTimestamp,
            route.referenceDistance,
            route.deviationDistance,
            route.deviationCount,
            route.osmSegmentCount,
            route.happy
          )
        }
      )
    }
  }
}
