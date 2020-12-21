package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorChangesPage
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorGroupChangesPage
import kpn.api.common.monitor.MonitorRouteChangesPage

trait MonitorRouteChangesPageBuilder {

  def changes(parameters: MonitorChangesParameters): Option[MonitorChangesPage]

  def groupChanges(groupName: String, parameters: MonitorChangesParameters): Option[MonitorGroupChangesPage]

  def routeChanges(routeId: Long, parameters: MonitorChangesParameters): Option[MonitorRouteChangesPage]

}
