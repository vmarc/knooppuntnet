package kpn.server.api.monitor

import kpn.api.common.monitor.RouteGroupsPage

trait MonitorAdminRouteGroupsPageBuilder {

  def build(): Option[RouteGroupsPage]

}
