package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorAdminGroupPage
import kpn.api.common.monitor.MonitorGroup
import kpn.api.common.monitor.MonitorGroupDetail
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.api.custom.ApiResponse
import kpn.core.common.TimestampLocal
import kpn.server.api.Api
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRepository
import kpn.server.repository.MonitorRouteRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component

@Component
class MonitorAdminFacadeImpl(
  api: Api,
  monitorRepository: MonitorRepository,
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository
) extends MonitorAdminFacade {

  override def groups(user: Option[String]): ApiResponse[MonitorGroupsPage] = {
    api.execute(user, "admin-groups", "") {
      val admin = monitorRepository.isAdminUser(user)
      val groups = monitorGroupRepository.groups()
      reply(
        Some(
          MonitorGroupsPage(
            admin,
            groups.map { group =>
              MonitorGroupDetail(
                group.name,
                group.description
              )
            }
          )
        )
      )
    }
  }

  override def group(user: Option[String], groupName: String): ApiResponse[MonitorAdminGroupPage] = {
    api.execute(user, "admin-group", "") {
      val admin = monitorRepository.isAdminUser(user)
      reply(
        monitorGroupRepository.group(groupName).map { group =>
          MonitorAdminGroupPage(
            admin,
            group.name,
            group.description
          )
        }
      )
    }
  }

  override def addGroup(user: Option[String], group: MonitorGroup): Unit = {
    api.execute(user, "admin-add-group", group.name) {
      assertAdminUser(user)
      monitorGroupRepository.saveGroup(group)
    }
  }

  override def updateGroup(user: Option[String], group: MonitorGroup): Unit = {
    api.execute(user, "admin-update-group", group.name) {
      assertAdminUser(user)
      monitorGroupRepository.saveGroup(group)
    }
  }

  override def deleteGroup(user: Option[String], groupName: String): Unit = {
    api.execute(user, "admin-delete-group", groupName) {
      assertAdminUser(user)
      monitorGroupRepository.deleteGroup(groupName)
    }
  }

  override def addRoute(user: Option[String], groupName: String, route: MonitorRoute): Unit = {
    api.execute(user, "admin-add-route", route.name) {
      assertAdminUser(user)
      monitorRouteRepository.saveRoute(route)
    }
  }

  override def updateRoute(user: Option[String], groupName: String, route: MonitorRoute): Unit = {
    api.execute(user, "admin-update-route", route.name) {
      assertAdminUser(user)
      monitorRouteRepository.saveRoute(route)
    }
  }

  override def deleteRoute(user: Option[String], groupName: String, routeName: String): Unit = {
    api.execute(user, "admin-delete-route", routeName) {
      assertAdminUser(user)
      monitorRouteRepository.deleteRoute(routeName)
    }
  }

  private def assertAdminUser(user: Option[String]): Unit = {
    if (!monitorRepository.isAdminUser(user)) {
      throw new AccessDeniedException("403 returned");
    }
  }

  private def reply[T](result: Option[T]): ApiResponse[T] = {
    val response = ApiResponse(null, 1, result)
    TimestampLocal.localize(response)
    response
  }
}
