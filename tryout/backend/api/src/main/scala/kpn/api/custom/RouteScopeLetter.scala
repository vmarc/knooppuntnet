package kpn.api.custom

import kpn.api.common.RouteScope

object RouteScopeLetter {
  def letter(scope: RouteScope): String = {
    scope match {
      case RouteScope.local => "l"
      case RouteScope.regional => "r"
      case RouteScope.national => "n"
      case RouteScope.international => "i"
      case RouteScope.unknown => "l"
    }
  }
}
