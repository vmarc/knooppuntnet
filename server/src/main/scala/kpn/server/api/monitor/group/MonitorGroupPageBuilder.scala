package kpn.server.api.monitor.group

import kpn.api.common.monitor.MonitorGroupPage

trait MonitorGroupPageBuilder {

  def build(groupName: String): Option[MonitorGroupPage]

}
