package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.api.common.NetworkType
import kpn.api.common.data.Way

class AccessibilityAnalyzerImpl extends AccessibilityAnalyzer {

  def accessible(networkType: NetworkType, way: Way): Boolean = {
    networkType match {
      case NetworkType.cycling => bicycleAccessible(way)
      case NetworkType.hiking => hikingAccessible(way)
      case NetworkType.horseRiding => horseAccessible(way)
      case NetworkType.motorboat => motorboatAccessible(way)
      case NetworkType.canoe => canoeAccessible(way)
      case NetworkType.inlineSkating => inlineSkatesAccessible(way)
      case _ => false
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
}
