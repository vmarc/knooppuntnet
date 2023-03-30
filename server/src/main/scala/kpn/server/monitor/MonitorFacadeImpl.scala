package kpn.server.monitor

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorChangesPage
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorGroupChangesPage
import kpn.api.common.monitor.MonitorGroupPage
import kpn.api.common.monitor.MonitorGroupProperties
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.api.common.monitor.MonitorRouteAddPage
import kpn.api.common.monitor.MonitorRouteChangePage
import kpn.api.common.monitor.MonitorRouteChangesPage
import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.api.common.monitor.MonitorRouteInfoPage
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.common.monitor.MonitorRouteUpdatePage
import kpn.api.custom.ApiResponse
import kpn.api.custom.Day
import kpn.core.common.TimestampLocal
import kpn.server.api.Api
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.group.MonitorGroupNamesBuilder
import kpn.server.monitor.group.MonitorGroupPageBuilder
import kpn.server.monitor.group.MonitorGroupsPageBuilder
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.route.MonitorRouteChangePageBuilder
import kpn.server.monitor.route.MonitorRouteChangesPageBuilder
import kpn.server.monitor.route.MonitorRouteDetailsPageBuilder
import kpn.server.monitor.route.MonitorRouteInfoBuilder
import kpn.server.monitor.route.MonitorRouteMapPageBuilder
import kpn.server.monitor.route.MonitorRouteUpdatePageBuilder
import kpn.server.monitor.route.MonitorUpdater
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component

import javax.servlet.http.HttpServletRequest
import scala.xml.Elem

@Component
class MonitorFacadeImpl(
  api: Api,
  monitorGroupsPageBuilder: MonitorGroupsPageBuilder,
  monitorGroupNamesBuilder: MonitorGroupNamesBuilder,
  monitorGroupPageBuilder: MonitorGroupPageBuilder,
  monitorRouteUpdatePageBuilder: MonitorRouteUpdatePageBuilder,
  monitorRouteDetailsPageBuilder: MonitorRouteDetailsPageBuilder,
  monitorRouteMapPageBuilder: MonitorRouteMapPageBuilder,
  monitorRouteChangesPageBuilder: MonitorRouteChangesPageBuilder,
  monitorRouteChangePageBuilder: MonitorRouteChangePageBuilder,
  monitorRouteInfoBuilder: MonitorRouteInfoBuilder,
  monitorRepository: MonitorRepository,
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdater: MonitorUpdater
) extends MonitorFacade {

  override def changes(
    request: HttpServletRequest,
    user: Option[String],
    parameters: MonitorChangesParameters
  ): ApiResponse[MonitorChangesPage] = {
    api.execute(request, user, "monitor-changes", "") {
      reply(monitorRouteChangesPageBuilder.changes(parameters))
    }
  }

  override def groups(
    request: HttpServletRequest,
    user: Option[String]
  ): ApiResponse[MonitorGroupsPage] = {
    api.execute(request, user, "monitor-groups", "") {
      reply(monitorGroupsPageBuilder.build(user))
    }
  }

  override def groupNames(
    request: HttpServletRequest,
    user: Option[String]
  ): ApiResponse[Seq[String]] = {
    api.execute(request, user, "monitor-group-names", "") {
      reply(Some(monitorGroupNamesBuilder.build()))
    }
  }

  override def group(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String
  ): ApiResponse[MonitorGroupPage] = {
    api.execute(request, user, "monitor-group", "") {
      reply(monitorGroupPageBuilder.build(user, groupName))
    }
  }

  override def groupAdd(
    request: HttpServletRequest,
    user: Option[String],
    properties: MonitorGroupProperties
  ): Unit = {
    api.execute(request, user, "monitor-add-group", properties.name) {
      assertAdminUser(user)
      monitorGroupRepository.saveGroup(MonitorGroup.from(properties))
    }
  }

  override def groupUpdate(
    request: HttpServletRequest,
    user: Option[String],
    id: ObjectId,
    properties: MonitorGroupProperties
  ): Unit = {
    api.execute(request, user, "monitor-update-group", properties.name) {
      assertAdminUser(user)
      monitorGroupRepository.saveGroup(MonitorGroup.from(id, properties))
    }
  }

  override def groupDelete(
    request: HttpServletRequest,
    user: Option[String],
    groupId: ObjectId
  ): Unit = {
    api.execute(request, user, "monitor-delete-group", groupId.oid) {
      assertAdminUser(user)
      monitorGroupRepository.deleteGroup(groupId)
    }
  }

  override def groupChanges(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String,
    parameters: MonitorChangesParameters
  ): ApiResponse[MonitorGroupChangesPage] = {
    api.execute(request, user, "monitor-group-changes", "") {
      reply(monitorRouteChangesPageBuilder.groupChanges(groupName, parameters))
    }
  }

  override def route(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String,
    routeName: String
  ): ApiResponse[MonitorRouteDetailsPage] = {
    val args = s"groupName=$groupName, routeName=$routeName"
    api.execute(request, user, "monitor-route", args) {
      reply(monitorRouteDetailsPageBuilder.build(groupName, routeName))
    }
  }

  override def routeMap(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String,
    routeName: String,
    relationId: Option[Long]
  ): ApiResponse[MonitorRouteMapPage] = {
    val args = relationId match {
      case Some(relationId) => s"$groupName:$routeName:$relationId"
      case None => s"$groupName:$routeName"
    }
    api.execute(request, user, "monitor-route-map", args) {
      reply(monitorRouteMapPageBuilder.build(groupName, routeName, relationId))
    }
  }

  override def routeChanges(
    request: HttpServletRequest,
    user: Option[String],
    monitorRouteId: String,
    parameters: MonitorChangesParameters
  ): ApiResponse[MonitorRouteChangesPage] = {
    val args = s"monitorRouteId=$monitorRouteId"
    api.execute(request, user, "monitor-route-changes", args) {
      reply(monitorRouteChangesPageBuilder.routeChanges(monitorRouteId, parameters))
    }
  }

  override def routeChange(
    request: HttpServletRequest,
    user: Option[String],
    routeId: Long,
    changeSetId: Long,
    replicationId: Long
  ): ApiResponse[MonitorRouteChangePage] = {
    val args = s"routeId=$routeId, changeSetId$changeSetId"
    api.execute(request, user, "monitor-route-change", args) {
      reply(monitorRouteChangePageBuilder.build(routeId, changeSetId, replicationId))
    }
  }

  override def routeInfo(
    request: HttpServletRequest,
    user: Option[String],
    routeId: Long
  ): ApiResponse[MonitorRouteInfoPage] = {
    api.execute(request, user, "monitor-route-info", routeId.toString) {
      assertAdminUser(user)
      reply(
        Some(
          monitorRouteInfoBuilder.build(routeId)
        )
      )
    }
  }

  override def groupRouteAdd(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String
  ): ApiResponse[MonitorRouteAddPage] = {
    api.execute(request, user, "monitor-group-route-add", groupName) {
      assertAdminUser(user)
      reply(
        monitorGroupRepository.groupByName(groupName).map { monitorGroup =>
          MonitorRouteAddPage(
            monitorGroup._id.oid,
            monitorGroup.name,
            monitorGroup.description
          )
        }
      )
    }
  }

  override def routeUpdatePage(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String,
    routeName: String
  ): ApiResponse[MonitorRouteUpdatePage] = {
    api.execute(request, user, "monitor-route-update-page", s"$groupName:$routeName") {
      assertAdminUser(user)
      reply(monitorRouteUpdatePageBuilder.build(groupName, routeName))
    }
  }

  override def routeAdd(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String,
    properties: MonitorRouteProperties
  ): ApiResponse[MonitorRouteSaveResult] = {
    api.execute(request, user, "monitor-route-add", properties.name) {
      assertAdminUser(user)
      reply(
        Some(
          monitorUpdater.add(user.get, groupName, properties)
        )
      )
    }
  }

  override def routeUpdate(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String,
    routeName: String,
    properties: MonitorRouteProperties
  ): ApiResponse[MonitorRouteSaveResult] = {
    api.execute(request, user, "monitor-route-update", s"$groupName:$routeName") {
      assertAdminUser(user)
      reply(
        Some(
          monitorUpdater.update(user.get, groupName, routeName, properties)
        )
      )
    }
  }

  override def routeDelete(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String,
    routeName: String
  ): Unit = {
    api.execute(request, user, "monitor-route-delete", s"$groupName:$routeName") {
      assertAdminUser(user)
      monitorGroupRepository.groupByName(groupName).foreach { group =>
        monitorRouteRepository.routeByName(group._id, routeName).foreach { route =>
          monitorRouteRepository.deleteRoute(route._id)
        }
      }
    }
  }

  override def upload(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String,
    routeName: String,
    relationId: Long,
    referenceDay: Day,
    filename: String,
    xml: Elem
  ): ApiResponse[MonitorRouteSaveResult] = {
    api.execute(request, user, "monitor-route-reference", s"$groupName:$routeName") {
      assertAdminUser(user)
      reply(
        Some(
          monitorUpdater.upload(user.get, groupName, routeName, relationId, referenceDay, filename, xml)
        )
      )
    }
  }

  override def routeNames(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String
  ): ApiResponse[Seq[String]] = {
    api.execute(request, user, "monitor-group-route-names", groupName) {
      reply(
        monitorGroupRepository.groupByName(groupName).map { group =>
          monitorRouteRepository.routeNames(group._id)
        }
      )
    }
  }

  private def assertAdminUser(user: Option[String]): Unit = {
    if (!monitorRepository.isAdminUser(user)) {
      throw new AccessDeniedException("403 returned")
    }
  }

  private def reply[T](result: Option[T]): ApiResponse[T] = {
    val response = ApiResponse(null, 1, result)
    TimestampLocal.localize(response)
    response
  }
}
