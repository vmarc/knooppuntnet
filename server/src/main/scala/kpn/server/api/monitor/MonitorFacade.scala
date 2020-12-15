package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorRouteChangePage
import kpn.api.common.monitor.MonitorRouteChangesPage
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.api.common.monitor.MonitorRoutesPage
import kpn.api.common.monitor.RouteGroupChangesPage
import kpn.api.common.monitor.RouteGroupDetailsPage
import kpn.api.common.monitor.RouteGroupsPage
import kpn.api.custom.ApiResponse

trait MonitorFacade {

  def groups(user: Option[String]): ApiResponse[RouteGroupsPage]

  def group(user: Option[String], groupName: String): ApiResponse[RouteGroupDetailsPage]

  def groupChanges(user: Option[String], groupName: String): ApiResponse[RouteGroupChangesPage]

  def routes(user: Option[String]): ApiResponse[MonitorRoutesPage]

  def route(user: Option[String], routeId: Long): ApiResponse[MonitorRouteDetailsPage]

  def routeMap(user: Option[String], routeId: Long): ApiResponse[MonitorRouteMapPage]

  def routeChanges(user: Option[String], routeId: Long): ApiResponse[MonitorRouteChangesPage]

  def routeChange(user: Option[String], routeId: Long, changeSetId: Long): ApiResponse[MonitorRouteChangePage]
}
