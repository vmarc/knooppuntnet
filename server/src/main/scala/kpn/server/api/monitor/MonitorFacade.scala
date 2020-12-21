package kpn.server.api.monitor

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

trait MonitorFacade {

  def groups(user: Option[String]): ApiResponse[MonitorGroupsPage]

  def group(user: Option[String], groupName: String): ApiResponse[MonitorGroupPage]

  def route(user: Option[String], routeId: Long): ApiResponse[MonitorRouteDetailsPage]

  def routeMap(user: Option[String], routeId: Long): ApiResponse[MonitorRouteMapPage]

  def changes(user: Option[String], parameters: MonitorChangesParameters): ApiResponse[MonitorChangesPage]

  def groupChanges(user: Option[String], groupName: String, parameters: MonitorChangesParameters): ApiResponse[MonitorGroupChangesPage]

  def routeChanges(user: Option[String], routeId: Long, parameters: MonitorChangesParameters): ApiResponse[MonitorRouteChangesPage]

  def routeChange(user: Option[String], routeId: Long, changeSetId: Long, replicationId: Long): ApiResponse[MonitorRouteChangePage]

}
