package kpn.server.api.monitor.route

import kpn.api.common.Language
import kpn.api.common.monitor.MonitorRouteMapPage

trait MonitorRouteMapPageBuilder {

  def build(language: Language, groupName: String, routeName: String): Option[MonitorRouteMapPage]

}
