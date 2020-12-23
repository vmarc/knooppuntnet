package kpn.server.api.monitor.admin

import kpn.api.common.monitor.MonitorGroupsPage

trait MonitorAdminGroupsPageBuilder {

  def build(): Option[MonitorGroupsPage]

}
