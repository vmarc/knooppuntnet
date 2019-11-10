package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.Location
import kpn.api.common.route.RouteInfo

//@Component
class RouteNodeBasedLocatorImpl extends RouteNodeBasedLocator {

  def locate(route: RouteInfo): Option[Location] = None

}
