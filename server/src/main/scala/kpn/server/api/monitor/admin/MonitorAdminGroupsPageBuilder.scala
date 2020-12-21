package kpn.server.api.monitor.admin

import kpn.api.common.monitor.MonitorAdminGroupsPage

trait MonitorAdminGroupsPageBuilder {

  def build(): Option[MonitorAdminGroupsPage]

}
