package kpn.server.api.monitor

import kpn.api.base.MongoId
import kpn.api.common.EN
import kpn.api.common.monitor.MonitorChangesPage
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorGroup
import kpn.api.common.monitor.MonitorGroupChangesPage
import kpn.api.common.monitor.MonitorGroupPage
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.api.common.monitor.MonitorRouteAdd
import kpn.api.common.monitor.MonitorRouteChangePage
import kpn.api.common.monitor.MonitorRouteChangesPage
import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.api.common.monitor.MonitorRouteInfoPage
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.custom.ApiResponse
import kpn.core.common.TimestampLocal
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalyzer
import kpn.server.api.Api
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.group.MonitorGroupPageBuilder
import kpn.server.api.monitor.group.MonitorGroupsPageBuilder
import kpn.server.api.monitor.route.MonitorRouteChangePageBuilder
import kpn.server.api.monitor.route.MonitorRouteChangesPageBuilder
import kpn.server.api.monitor.route.MonitorRouteDetailsPageBuilder
import kpn.server.api.monitor.route.MonitorRouteInfoBuilder
import kpn.server.api.monitor.route.MonitorRouteMapPageBuilder
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRepository
import kpn.server.repository.MonitorRouteRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component

import scala.xml.Elem

@Component
class MonitorFacadeImpl(
  api: Api,
  monitorGroupsPageBuilder: MonitorGroupsPageBuilder,
  monitorGroupPageBuilder: MonitorGroupPageBuilder,
  monitorRouteDetailsPageBuilder: MonitorRouteDetailsPageBuilder,
  monitorRouteMapPageBuilder: MonitorRouteMapPageBuilder,
  monitorRouteChangesPageBuilder: MonitorRouteChangesPageBuilder,
  monitorRouteChangePageBuilder: MonitorRouteChangePageBuilder,
  monitorRouteInfoBuilder: MonitorRouteInfoBuilder,
  monitorRepository: MonitorRepository,
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorRouteAnalyzer: MonitorRouteAnalyzer
) extends MonitorFacade {

  override def changes(user: Option[String], parameters: MonitorChangesParameters): ApiResponse[MonitorChangesPage] = {
    api.execute(user, "monitor-changes", "") {
      reply(monitorRouteChangesPageBuilder.changes(parameters))
    }
  }

  override def groups(user: Option[String]): ApiResponse[MonitorGroupsPage] = {
    api.execute(user, "monitor-groups", "") {
      reply(monitorGroupsPageBuilder.build(user))
    }
  }

  override def group(user: Option[String], groupName: String): ApiResponse[MonitorGroupPage] = {
    api.execute(user, "monitor-group", "") {
      reply(monitorGroupPageBuilder.build(user, groupName))
    }
  }

  //  override def group(user: Option[String], groupName: String): ApiResponse[MonitorAdminGroupPage] = {
  //    api.execute(user, "admin-group", "") {
  //      val admin = monitorRepository.isAdminUser(user)
  //      reply(
  //        monitorGroupRepository.group(groupName).map { group =>
  //          MonitorAdminGroupPage(
  //            admin,
  //            group.name,
  //            group.description
  //          )
  //        }
  //      )
  //    }
  //  }

  override def addGroup(user: Option[String], group: MonitorGroup): Unit = {
    api.execute(user, "monitor-add-group", group.name) {
      assertAdminUser(user)
      monitorGroupRepository.saveGroup(group)
    }
  }

  override def updateGroup(user: Option[String], group: MonitorGroup): Unit = {
    api.execute(user, "monitor-update-group", group.name) {
      assertAdminUser(user)
      monitorGroupRepository.saveGroup(group)
    }
  }

  override def deleteGroup(user: Option[String], groupId: MongoId): Unit = {
    api.execute(user, "monitor-delete-group", groupId.oid) {
      assertAdminUser(user)
      monitorGroupRepository.deleteGroup(groupId)
    }
  }

  override def groupChanges(user: Option[String], groupName: String, parameters: MonitorChangesParameters): ApiResponse[MonitorGroupChangesPage] = {
    api.execute(user, "monitor-group-changes", "") {
      reply(monitorRouteChangesPageBuilder.groupChanges(groupName, parameters))
    }
  }

  override def route(user: Option[String], monitorRouteId: String): ApiResponse[MonitorRouteDetailsPage] = {
    val args = s"monitorRouteId=$monitorRouteId"
    api.execute(user, "monitor-route", args) {
      reply(monitorRouteDetailsPageBuilder.build(monitorRouteId))
    }
  }

  override def routeMap(user: Option[String], monitorRouteId: String): ApiResponse[MonitorRouteMapPage] = {
    val args = s"monitorRouteId=$monitorRouteId"
    api.execute(user, "monitor-route-map", args) {
      reply(monitorRouteMapPageBuilder.build(monitorRouteId, EN))
    }
  }

  override def routeChanges(user: Option[String], monitorRouteId: String, parameters: MonitorChangesParameters): ApiResponse[MonitorRouteChangesPage] = {
    val args = s"monitorRouteId=$monitorRouteId"
    api.execute(user, "monitor-route-changes", args) {
      reply(monitorRouteChangesPageBuilder.routeChanges(monitorRouteId, parameters))
    }
  }

  override def routeChange(user: Option[String], routeId: Long, changeSetId: Long, replicationId: Long): ApiResponse[MonitorRouteChangePage] = {
    val args = s"routeId=$routeId, changeSetId$changeSetId"
    api.execute(user, "monitor-route-change", args) {
      reply(monitorRouteChangePageBuilder.build(routeId, changeSetId, replicationId))
    }
  }

  override def routeInfo(user: Option[String], routeId: Long): ApiResponse[MonitorRouteInfoPage] = {
    api.execute(user, "monitor-route-info", routeId.toString) {
      assertAdminUser(user)
      reply(
        Some(
          monitorRouteInfoBuilder.build(routeId)
        )
      )
    }
  }

  override def addRoute(user: Option[String], add: MonitorRouteAdd): Unit = {
    api.execute(user, "monitor-add-route", add.name) {
      assertAdminUser(user)
          val route = MonitorRoute(
            MongoId(),
            add.groupId,
            add.name,
            add.description,
            add.relationId,
          )
          monitorRouteRepository.saveRoute(route)

    }
  }

  override def updateRoute(user: Option[String], route: MonitorRoute): Unit = {
    api.execute(user, "monitor-update-route", route.name) {
      assertAdminUser(user)
      monitorRouteRepository.saveRoute(route)
    }
  }

  override def deleteRoute(user: Option[String], routeId: MongoId): Unit = {
    api.execute(user, "monitor-delete-route", routeId.oid) {
      assertAdminUser(user)
      monitorRouteRepository.deleteRoute(routeId)
    }
  }

  override def processNewReference(user: Option[String], groupName: String, routeName: String, filename: String, xml: Elem): Unit = {
    val routeId = MongoId("TODO MON") // groupName + ":" + routeName
    api.execute(user, "monitor-route-reference", routeId.oid) {
      assertAdminUser(user)
      monitorRouteAnalyzer.processNewReference(user.get, routeId, filename, xml)
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
