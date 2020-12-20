package kpn.server.api.monitor.group

import kpn.api.common.monitor.RouteGroupsPage

trait MonitorGroupsPageBuilder {

  def build(): Option[RouteGroupsPage]

}
