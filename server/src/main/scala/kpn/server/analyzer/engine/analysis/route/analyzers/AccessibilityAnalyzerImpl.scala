package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.data.Way
import kpn.api.custom.NetworkType

class AccessibilityAnalyzerImpl extends AccessibilityAnalyzer {

  def accessible(networkType: NetworkType, way: Way): Boolean = {
    networkType match {
      case NetworkType.cycling => bicycleAccessible(way)
      case NetworkType.hiking => hikingAccessible(way)
      case NetworkType.horseRiding => horseAccessible(way)
      case NetworkType.motorboat => motorboatAccessible(way)
      case NetworkType.canoe => canoeAccessible(way)
      case NetworkType.inlineSkating => inlineSkatesAccessible(way)
    }
  }

  private def bicycleAccessible(way: Way): Boolean = {
    (way.tags.has("highway") ||
      way.tags.has("highway:virtual") ||
      way.tags.has("route", "ferry") ||
      way.tags.has("bicycle", "yes")) &&
      !way.tags.has("bicycle", "no")
  }

  private def hikingAccessible(way: Way): Boolean = {
    (way.tags.has("highway") ||
      way.tags.has("highway:virtual") ||
      way.tags.has("route", "ferry") ||
      way.tags.has("foot", "yes")) &&
      !way.tags.has("foot", "no")
  }

  private def horseAccessible(way: Way): Boolean = {
    (way.tags.has("highway") ||
      way.tags.has("highway:virtual") ||
      way.tags.has("route", "ferry") ||
      way.tags.has("horse", "yes")) &&
      !way.tags.has("horse", "no")
  }

  private def motorboatAccessible(way: Way): Boolean = {
    way.tags.has("waterway") ||
      way.tags.has("waterway:virtual")
  }

  private def canoeAccessible(way: Way): Boolean = {
    (way.tags.has("waterway") ||
      way.tags.has("waterway:virtual") ||
      way.tags.has("canoe", "portage") || // used for places where canoe has to be carried over a dam
      way.tags.has("canoe", "yes")) &&
      !way.tags.has("canoe", "no")
  }

  private def inlineSkatesAccessible(way: Way): Boolean = {
    (way.tags.has("highway") ||
      way.tags.has("highway:virtual") ||
      way.tags.has("route", "ferry") ||
      way.tags.has("inline_skates", "yes")) &&
      !way.tags.has("inline_skates", "no")
  }

}
