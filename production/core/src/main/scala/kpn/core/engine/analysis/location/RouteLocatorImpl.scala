package kpn.core.engine.analysis.location

import kpn.shared.RouteLocationAnalysis
import kpn.shared.route.RouteInfo

/*
  Determines the location of a given route.

  First attempts to determine the route location based on the route nodes.  If that does
  not result in a unique Location, the tries to use the route way based locator.
 */
class RouteLocatorImpl(nodeBasedLocator: RouteNodeBasedLocator, wayBasedLocator: RouteWayBasedLocator) extends RouteLocator {

  def locate(route: RouteInfo): Option[RouteLocationAnalysis] = {
    nodeBasedLocator.locate(route) match {
      case Some(location) => Some(RouteLocationAnalysis(location))
      case None => wayBasedLocator.locate(route)
    }
  }

}
