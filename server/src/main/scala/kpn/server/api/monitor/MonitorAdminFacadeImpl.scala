package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorAdminGroupPage
import kpn.api.common.monitor.MonitorGroup
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.api.custom.ApiResponse
import kpn.core.common.TimestampLocal
import kpn.server.api.Api
import kpn.server.api.monitor.admin.MonitorAdminGroupsPageBuilder
import kpn.server.repository.MonitorAdminGroupRepository
import org.springframework.stereotype.Component

@Component
class MonitorAdminFacadeImpl(
  api: Api,
  monitorAdminGroupRepository: MonitorAdminGroupRepository,
  monitorAdminGroupsPageBuilder: MonitorAdminGroupsPageBuilder
) extends MonitorAdminFacade {

  override def groups(user: Option[String]): ApiResponse[MonitorGroupsPage] = {
    api.execute(user, "admin-groups", "") {
      reply(monitorAdminGroupsPageBuilder.build())
    }
  }

  override def group(user: Option[String], groupName: String): ApiResponse[MonitorAdminGroupPage] = {
    api.execute(user, "admin-group", "") {
      reply(
        monitorAdminGroupRepository.group(groupName).map { group =>
          MonitorAdminGroupPage(
            group.name,
            group.description
          )
        }
      )
    }
  }

  override def addGroup(user: Option[String], group: MonitorGroup): Unit = {
    api.execute(user, "admin-add-group", group.name) {
      monitorAdminGroupRepository.saveGroup(group)
    }
  }

  override def updateGroup(user: Option[String], group: MonitorGroup): Unit = {
    api.execute(user, "admin-update-group", group.name) {
      monitorAdminGroupRepository.saveGroup(group)
    }
  }

  override def deleteGroup(user: Option[String], groupName: String): Unit = {
    api.execute(user, "admin-delete-group", groupName) {
      monitorAdminGroupRepository.deleteGroup(groupName)
    }
  }

  private def reply[T](result: Option[T]): ApiResponse[T] = {
    val response = ApiResponse(null, 1, result)
    TimestampLocal.localize(response)
    response
  }

}
