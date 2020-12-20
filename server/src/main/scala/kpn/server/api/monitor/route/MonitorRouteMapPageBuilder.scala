package kpn.server.api.monitor.route

import kpn.api.common.Language
import kpn.api.common.monitor.MonitorRouteMapPage

trait MonitorRouteMapPageBuilder {

  def build(routeId: Long, language: Language): Option[MonitorRouteMapPage]

}
