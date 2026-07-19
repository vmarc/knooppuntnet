package kpn.api.custom

import kpn.api.common.RouteType

object RouteTypeLetter {

  def letter(routeType: RouteType): String = {
    routeType match {
      case RouteType.hiking => "w"
      case RouteType.cycling => "c"
      case RouteType.horseRiding => "h"
      case RouteType.canoe => "p"
      case RouteType.motorboat => "m"
      case RouteType.inlineSkating => "i"
      case RouteType.mtb => "?"
    }
  }
}
