package kpn.server.api.monitor.group

import kpn.api.common.monitor.MonitorGroupsPage

trait MonitorGroupsPageBuilder {

  def build(user: Option[String]): Option[MonitorGroupsPage]

}
