package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorRoutesPage

trait MonitorRoutesPageBuilder {

  def build(): Option[MonitorRoutesPage]

}
