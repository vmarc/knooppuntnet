package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorRouteChangePage
import kpn.api.common.monitor.MonitorRouteChangesPage
import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRoutesPage
import kpn.api.common.monitor.RouteGroupChangesPage
import kpn.api.common.monitor.RouteGroupDetailsPage
import kpn.api.common.monitor.RouteGroupsPage
import kpn.api.custom.ApiResponse
import kpn.core.common.TimestampLocal
import kpn.server.api.Api
import org.springframework.stereotype.Component

@Component
class MonitorFacadeImpl(
  api: Api,
  monitorRoutesPageBuilder: MonitorRoutesPageBuilder,
  monitorRouteDetailsPageBuilder: MonitorRouteDetailsPageBuilder,
  monitorRouteMapPageBuilder: MonitorRouteMapPageBuilder,
  monitorRouteChangesPageBuilder: MonitorRouteChangesPageBuilder,
  monitorRouteChangePageBuilder: MonitorRouteChangePageBuilder
) extends MonitorFacade {

  override def groups(user: Option[String]): ApiResponse[RouteGroupsPage] = {
    api.execute(user, "monitor-groups", "") {
      reply(Some(RouteGroupsPage()))
    }
  }

  override def group(user: Option[String], groupName: String): ApiResponse[RouteGroupDetailsPage] = {
    api.execute(user, "monitor-group", "") {
      reply(Some(RouteGroupDetailsPage()))
    }
  }

  override def groupChanges(user: Option[String], groupName: String): ApiResponse[RouteGroupChangesPage] = {
    api.execute(user, "monitor-group-changes", "") {
      reply(Some(RouteGroupChangesPage()))
    }
  }

  override def routes(user: Option[String]): ApiResponse[MonitorRoutesPage] = {
    api.execute(user, "monitor-routes", "") {
      reply(monitorRoutesPageBuilder.build())
    }
  }

  override def route(user: Option[String], routeId: Long): ApiResponse[MonitorRouteDetailsPage] = {
    val args = s"routeId=$routeId"
    api.execute(user, "monitor-route", args) {
      reply(monitorRouteDetailsPageBuilder.build(routeId))
    }
  }

  override def routeMap(user: Option[String], routeId: Long): ApiResponse[MonitorRouteMapPage] = {
    val args = s"routeId=$routeId"
    api.execute(user, "monitor-route-map", args) {
      reply(monitorRouteMapPageBuilder.build(routeId))
    }
  }

  override def routeChanges(user: Option[String], routeId: Long): ApiResponse[MonitorRouteChangesPage] = {
    val args = s"routeId=$routeId"
    api.execute(user, "monitor-route-changes", args) {
      reply(monitorRouteChangesPageBuilder.build(routeId))
    }
  }

  override def routeChange(user: Option[String], routeId: Long, changeSetId: Long): ApiResponse[MonitorRouteChangePage] = {
    val args = s"routeId=$routeId, changeSetId$changeSetId"
    api.execute(user, "monitor-route-change", args) {
      reply(monitorRouteChangePageBuilder.build(routeId, changeSetId))
    }
  }

  private def reply[T](result: Option[T]): ApiResponse[T] = {
    val response = ApiResponse(null, 1, result)
    TimestampLocal.localize(response)
    response
  }

}
