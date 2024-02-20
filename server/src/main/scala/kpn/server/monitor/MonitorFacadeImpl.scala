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
import kpn.api.common.monitor.MonitorRouteGpxPage
import kpn.api.common.monitor.MonitorRouteInfoPage
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRouteUpdatePage
import kpn.api.custom.ApiResponse
import kpn.core.common.TimestampLocal
import kpn.server.api.Api
import kpn.server.config.RequestContext
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
import kpn.server.monitor.route.MonitorRouteGpxPageBuilder
import kpn.server.monitor.route.MonitorRouteInfoBuilder
import kpn.server.monitor.route.MonitorRouteMapPageBuilder
import kpn.server.monitor.route.MonitorRouteUpdatePageBuilder
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component

@Component
class MonitorFacadeImpl(
  api: Api,
  monitorGroupsPageBuilder: MonitorGroupsPageBuilder,
  monitorGroupNamesBuilder: MonitorGroupNamesBuilder,
  monitorGroupPageBuilder: MonitorGroupPageBuilder,
  monitorRouteUpdatePageBuilder: MonitorRouteUpdatePageBuilder,
  monitorRouteDetailsPageBuilder: MonitorRouteDetailsPageBuilder,
  monitorRouteMapPageBuilder: MonitorRouteMapPageBuilder,
  monitorRouteGpxPageBuilder: MonitorRouteGpxPageBuilder,
  monitorRouteChangesPageBuilder: MonitorRouteChangesPageBuilder,
  monitorRouteChangePageBuilder: MonitorRouteChangePageBuilder,
  monitorRouteInfoBuilder: MonitorRouteInfoBuilder,
  monitorRepository: MonitorRepository,
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository
) extends MonitorFacade {

  override def changes(parameters: MonitorChangesParameters): ApiResponse[MonitorChangesPage] = {
    api.execute("monitor-changes", "") {
      reply(monitorRouteChangesPageBuilder.changes(parameters))
    }
  }

  override def groups(): ApiResponse[MonitorGroupsPage] = {
    api.execute("monitor-groups", "") {
      reply(monitorGroupsPageBuilder.build())
    }
  }

  override def groupNames(): ApiResponse[Seq[String]] = {
    api.execute("monitor-group-names", "") {
      reply(Some(monitorGroupNamesBuilder.build()))
    }
  }

  override def group(groupName: String): ApiResponse[MonitorGroupPage] = {
    api.execute("monitor-group", "") {
      reply(monitorGroupPageBuilder.build(groupName))
    }
  }

  override def groupAdd(properties: MonitorGroupProperties): Unit = {
    api.execute("monitor-add-group", properties.name) {
      assertAdminUser(RequestContext.user)
      monitorGroupRepository.saveGroup(MonitorGroup.from(properties))
    }
  }

  override def groupUpdate(id: ObjectId, properties: MonitorGroupProperties): Unit = {
    api.execute("monitor-update-group", properties.name) {
      assertAdminUser(RequestContext.user)
      monitorGroupRepository.saveGroup(MonitorGroup.from(id, properties))
    }
  }

  override def groupDelete(groupId: ObjectId): Unit = {
    api.execute("monitor-delete-group", groupId.oid) {
      assertAdminUser(RequestContext.user)
      monitorGroupRepository.deleteGroup(groupId)
    }
  }

  override def groupChanges(groupName: String, parameters: MonitorChangesParameters): ApiResponse[MonitorGroupChangesPage] = {
    api.execute("monitor-group-changes", "") {
      reply(monitorRouteChangesPageBuilder.groupChanges(groupName, parameters))
    }
  }

  override def route(groupName: String, routeName: String): ApiResponse[MonitorRouteDetailsPage] = {
    val args = s"groupName=$groupName, routeName=$routeName"
    api.execute("monitor-route", args) {
      reply(monitorRouteDetailsPageBuilder.build(groupName, routeName))
    }
  }

  override def routeMap(groupName: String, routeName: String, subRelationIndex: Option[Int]): ApiResponse[MonitorRouteMapPage] = {
    val args = subRelationIndex match {
      case Some(index) => s"$groupName:$routeName:$index"
      case None => s"$groupName:$routeName"
    }
    api.execute("monitor-route-map", args) {
      reply(monitorRouteMapPageBuilder.build(groupName, routeName, subRelationIndex))
    }
  }

  override def routeGpx(groupName: String, routeName: String, subRelationId: Long): ApiResponse[MonitorRouteGpxPage] = {
    val args = s"groupName=$groupName, routeName=$routeName, subRelationId=$subRelationId"
    api.execute("monitor-route-gpx", args) {
      reply(monitorRouteGpxPageBuilder.build(groupName, routeName, subRelationId))
    }
  }

  override def routeChanges(monitorRouteId: String, parameters: MonitorChangesParameters): ApiResponse[MonitorRouteChangesPage] = {
    val args = s"monitorRouteId=$monitorRouteId"
    api.execute("monitor-route-changes", args) {
      reply(monitorRouteChangesPageBuilder.routeChanges(monitorRouteId, parameters))
    }
  }

  override def routeChange(routeId: Long, changeSetId: Long, replicationId: Long): ApiResponse[MonitorRouteChangePage] = {
    val args = s"routeId=$routeId, changeSetId$changeSetId"
    api.execute("monitor-route-change", args) {
      reply(monitorRouteChangePageBuilder.build(routeId, changeSetId, replicationId))
    }
  }

  override def routeInfo(routeId: Long): ApiResponse[MonitorRouteInfoPage] = {
    api.execute("monitor-route-info", routeId.toString) {
      assertAdminUser(RequestContext.user)
      reply(
        Some(
          monitorRouteInfoBuilder.build(routeId)
        )
      )
    }
  }

  override def groupRouteAdd(groupName: String): ApiResponse[MonitorRouteAddPage] = {
    api.execute("monitor-group-route-add", groupName) {
      assertAdminUser(RequestContext.user)
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

  override def routeUpdatePage(groupName: String, routeName: String): ApiResponse[MonitorRouteUpdatePage] = {
    api.execute("monitor-route-update-page", s"$groupName:$routeName") {
      assertAdminUser(RequestContext.user)
      reply(monitorRouteUpdatePageBuilder.build(groupName, routeName))
    }
  }

  override def routeDelete(groupName: String, routeName: String): Unit = {
    api.execute("monitor-route-delete", s"$groupName:$routeName") {
      assertAdminUser(RequestContext.user)
      monitorGroupRepository.groupByName(groupName).foreach { group =>
        monitorRouteRepository.routeByName(group._id, routeName).foreach { route =>
          monitorRouteRepository.deleteRoute(route._id)
        }
      }
    }
  }

  override def routeNames(groupName: String): ApiResponse[Seq[String]] = {
    api.execute("monitor-group-route-names", groupName) {
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
