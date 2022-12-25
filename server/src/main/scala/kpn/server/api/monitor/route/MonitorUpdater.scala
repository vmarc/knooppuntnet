package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult

trait MonitorUpdater {

  def add(user: String, groupName: String, properties: MonitorRouteProperties): MonitorRouteSaveResult

  def update(user: String, groupName: String, routeName: String, properties: MonitorRouteProperties): MonitorRouteSaveResult
}
