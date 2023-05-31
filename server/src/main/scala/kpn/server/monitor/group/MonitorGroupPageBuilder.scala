package kpn.server.monitor.group

import kpn.api.common.monitor.MonitorGroupPage
import kpn.api.common.monitor.MonitorRouteDetail
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
      val routes = monitorGroupRepository.groupRoutes(group._id).sortBy(_.name)
      MonitorGroupPage(
        group._id.oid,
        groupName,
        group.description,
        admin,
        routes.zipWithIndex.map { case (route, rowIndex) =>
          MonitorRouteDetail(
            rowIndex,
            route._id.oid,
            route.name,
            route.description,
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
