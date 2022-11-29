package kpn.server.api.monitor.group

import kpn.api.common.monitor.MonitorGroupPage
import kpn.api.common.monitor.MonitorRouteDetail
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRepository
import org.springframework.stereotype.Component

@Component
class MonitorGroupPageBuilder(
  monitorRepository: MonitorRepository,
  monitorGroupRepository: MonitorGroupRepository
) {

  def build(user: Option[String], groupName: String): Option[MonitorGroupPage] = {
    val admin = monitorRepository.isAdminUser(user)
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
            route.referenceDay,
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
