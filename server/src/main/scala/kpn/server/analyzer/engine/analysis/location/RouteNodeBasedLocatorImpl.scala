package kpn.server.analyzer.engine.analysis.location

import kpn.shared.Location
import kpn.shared.route.RouteInfo
import org.springframework.stereotype.Component

//@Component
class RouteNodeBasedLocatorImpl extends RouteNodeBasedLocator {

  def locate(route: RouteInfo): Option[Location] = None

}
