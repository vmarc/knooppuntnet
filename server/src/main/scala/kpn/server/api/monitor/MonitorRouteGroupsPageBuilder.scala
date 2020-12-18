package kpn.server.api.monitor

import kpn.api.common.monitor.RouteGroupsPage

trait MonitorRouteGroupsPageBuilder {

  def build(): Option[RouteGroupsPage]

}
