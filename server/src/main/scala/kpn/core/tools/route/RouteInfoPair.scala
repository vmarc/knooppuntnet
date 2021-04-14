package kpn.core.tools.route

import kpn.api.common.route.RouteInfo

case class RouteInfoPair(oldRoute: RouteInfo, newRoute: RouteInfo) {
  def isIdentical: Boolean = oldRoute == newRoute
}
