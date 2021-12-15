package kpn.server.api.monitor

import kpn.api.common.EN
import kpn.api.common.monitor.MonitorChangesPage
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorGroupChangesPage
import kpn.api.common.monitor.MonitorGroupPage
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.api.common.monitor.MonitorRouteChangePage
import kpn.api.common.monitor.MonitorRouteChangesPage
import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.custom.ApiResponse
import kpn.core.common.TimestampLocal
import kpn.server.api.Api
import kpn.server.api.monitor.group.MonitorGroupPageBuilder
import kpn.server.api.monitor.group.MonitorGroupsPageBuilder
import kpn.server.api.monitor.route.MonitorRouteChangePageBuilder
import kpn.server.api.monitor.route.MonitorRouteChangesPageBuilder
import kpn.server.api.monitor.route.MonitorRouteDetailsPageBuilder
import kpn.server.api.monitor.route.MonitorRouteMapPageBuilder
import org.springframework.stereotype.Component

@Component
class MonitorFacadeImpl(
  api: Api,
  monitorGroupsPageBuilder: MonitorGroupsPageBuilder,
  monitorGroupPageBuilder: MonitorGroupPageBuilder,
  monitorRouteDetailsPageBuilder: MonitorRouteDetailsPageBuilder,
  monitorRouteMapPageBuilder: MonitorRouteMapPageBuilder,
  monitorRouteChangesPageBuilder: MonitorRouteChangesPageBuilder,
  monitorRouteChangePageBuilder: MonitorRouteChangePageBuilder
) extends MonitorFacade {

  override def groups(user: Option[String]): ApiResponse[MonitorGroupsPage] = {
    api.execute(user, "monitor-groups", "") {
      reply(monitorGroupsPageBuilder.build())
    }
  }

  override def group(user: Option[String], groupName: String): ApiResponse[MonitorGroupPage] = {
    api.execute(user, "monitor-group", "") {
      reply(monitorGroupPageBuilder.build(groupName))
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

  override def changes(user: Option[String], parameters: MonitorChangesParameters): ApiResponse[MonitorChangesPage] = {
    api.execute(user, "monitor-changes", "") {
      reply(monitorRouteChangesPageBuilder.changes(parameters))
    }
  }

  override def groupChanges(user: Option[String], groupName: String, parameters: MonitorChangesParameters): ApiResponse[MonitorGroupChangesPage] = {
    api.execute(user, "monitor-group-changes", "") {
      reply(monitorRouteChangesPageBuilder.groupChanges(groupName, parameters))
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

  private def reply[T](result: Option[T]): ApiResponse[T] = {
    val response = ApiResponse(null, 1, result)
    TimestampLocal.localize(response)
    response
  }
}
