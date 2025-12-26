package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.RouteType
import kpn.api.common.data.Way

class AccessibilityAnalyzer {

  def accessible(role: Option[String], routeType: RouteType, way: Way): Boolean = {
    if (RouteRoleAnalyzer.isPoiRole(role)) {
      true
    }
    else {
      routeType match {
        case RouteType.cycling => bicycleAccessible(way)
        case RouteType.hiking => hikingAccessible(way)
        case RouteType.horseRiding => horseAccessible(way)
        case RouteType.motorboat => motorboatAccessible(way)
        case RouteType.canoe => canoeAccessible(way)
        case RouteType.inlineSkating => inlineSkatesAccessible(way)
        case RouteType.mtb => mtbAccessible(way)
        case _ => false
      }
    }
  }

  private def bicycleAccessible(way: Way): Boolean = {
    (way.hasTag("highway") ||
      way.hasTag("highway:virtual") ||
      way.hasTag("route", "ferry") ||
      way.hasTag("bicycle", "yes")) &&
      !way.hasTag("bicycle", "no")
  }

  private def hikingAccessible(way: Way): Boolean = {
    (way.hasTag("highway") ||
      way.hasTag("highway:virtual") ||
      way.hasTag("route", "ferry") ||
      way.hasTag("foot", "yes")) &&
      !way.hasTag("foot", "no")
  }

  private def horseAccessible(way: Way): Boolean = {
    (way.hasTag("highway") ||
      way.hasTag("highway:virtual") ||
      way.hasTag("route", "ferry") ||
      way.hasTag("horse", "yes")) &&
      !way.hasTag("horse", "no")
  }

  private def motorboatAccessible(way: Way): Boolean = {
    way.hasTag("waterway") ||
      way.hasTag("waterway:virtual")
  }

  private def canoeAccessible(way: Way): Boolean = {
    (way.hasTag("waterway") ||
      way.hasTag("waterway:virtual") ||
      way.hasTag("canoe", "portage") || // used for places where canoe has to be carried over a dam
      way.hasTag("canoe", "yes")) &&
      !way.hasTag("canoe", "no")
  }

  private def inlineSkatesAccessible(way: Way): Boolean = {
    (way.hasTag("highway") ||
      way.hasTag("highway:virtual") ||
      way.hasTag("route", "ferry") ||
      way.hasTag("inline_skates", "yes")) &&
      !way.hasTag("inline_skates", "no")
  }

  private def mtbAccessible(way: Way): Boolean = {
    (way.hasTag("highway") ||
      way.hasTag("highway:virtual") ||
      way.hasTag("route", "ferry") ||
      way.hasTag("mtb", "yes")) &&
      !way.hasTag("mtb", "no")
  }
}
