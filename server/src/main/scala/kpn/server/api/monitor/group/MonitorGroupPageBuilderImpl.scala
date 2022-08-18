package kpn.server.api.monitor.group

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorGroupPage
import kpn.api.common.monitor.MonitorRouteDetail
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRepository
import org.springframework.stereotype.Component

@Component
class MonitorGroupPageBuilderImpl(
  monitorRepository: MonitorRepository,
  monitorGroupRepository: MonitorGroupRepository
) extends MonitorGroupPageBuilder {

  override def build(user: Option[String], groupName: String): Option[MonitorGroupPage] = {
    val admin = monitorRepository.isAdminUser(user)
    monitorGroupRepository.groupByName(groupName).map { group =>
      val routes = monitorGroupRepository.groupRoutes(group._id)
      MonitorGroupPage(
        group._id.oid,
        groupName,
        group.description,
        admin,
        routes.zipWithIndex.map { case (route, index) =>
          MonitorRouteDetail(
            index,
            route._id.oid,
            route.name,
            route.description,
            route.relationId,
            wayCount = 0,
            osmDistance = 0,
            gpxDistance = 0,
            gpxFilename = None,
            osmHappy = false,
            gpxHappy = false,
            happy = true
          )
        }
      )
    }
  }
}
