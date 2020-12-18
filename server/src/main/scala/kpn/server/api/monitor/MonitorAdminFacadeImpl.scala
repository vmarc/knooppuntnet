package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorAdminRouteGroupPage
import kpn.api.common.monitor.MonitorRouteGroup
import kpn.api.common.monitor.RouteGroupsPage
import kpn.api.custom.ApiResponse
import kpn.core.common.TimestampLocal
import kpn.server.api.Api
import kpn.server.repository.MonitorAdminRouteGroupRepository
import org.springframework.stereotype.Component

@Component
class MonitorAdminFacadeImpl(
  api: Api,
  monitorAdminRouteGroupRepository: MonitorAdminRouteGroupRepository,
  monitorAdminRouteGroupsPageBuilder: MonitorAdminRouteGroupsPageBuilder
) extends MonitorAdminFacade {

  override def groups(user: Option[String]): ApiResponse[RouteGroupsPage] = {
    api.execute(user, "monitor-groups", "") {
      reply(monitorAdminRouteGroupsPageBuilder.build())
    }
  }

  override def group(user: Option[String], groupName: String): ApiResponse[MonitorAdminRouteGroupPage] = {
    api.execute(user, "get-group", "") {
      reply(
        monitorAdminRouteGroupRepository.group(groupName).map { group =>
          MonitorAdminRouteGroupPage(
            group.name,
            group.description
          )
        }
      )
    }
  }

  override def addGroup(user: Option[String], group: MonitorRouteGroup): Unit = {
    api.execute(user, "add-group", group.name) {
      monitorAdminRouteGroupRepository.saveGroup(group)
    }
  }

  override def updateGroup(user: Option[String], group: MonitorRouteGroup): Unit = {
    api.execute(user, "update-group", group.name) {
      monitorAdminRouteGroupRepository.saveGroup(group)
    }
  }

  override def deleteGroup(user: Option[String], groupName: String): Unit = {
    api.execute(user, "delete-group", groupName) {
      monitorAdminRouteGroupRepository.deleteGroup(groupName)
    }
  }

  private def reply[T](result: Option[T]): ApiResponse[T] = {
    val response = ApiResponse(null, 1, result)
    TimestampLocal.localize(response)
    response
  }

}
