package kpn.core.engine.analysis.route.analyzers

import kpn.shared.NetworkType
import kpn.shared.data.Way

class AccessibilityAnalyzerImpl extends AccessibilityAnalyzer {

  def accessible(networkType: NetworkType, way: Way): Boolean = {
    networkType match {
      case NetworkType.bicycle => bicycleAccessible(way)
      case NetworkType.hiking => hikingAccessible(way)
      case NetworkType.horse => horseAccessible(way)
      case NetworkType.motorboat => motorboatAccessible(way)
      case NetworkType.canoe => canoeAccessible(way)
      case NetworkType.inlineSkates => inlineSkatesAccessible(way)
    }
  }

  private def bicycleAccessible(way: Way): Boolean = {
    way.tags.has("highway") ||
      way.tags.has("route", "ferry") ||
      way.tags.has("bicycle", "yes")
  }

  private def hikingAccessible(way: Way): Boolean = {
    way.tags.has("highway") ||
      way.tags.has("route", "ferry") ||
      way.tags.has("foot", "yes")
  }

  private def horseAccessible(way: Way): Boolean = {
    way.tags.has("highway") ||
      way.tags.has("route", "ferry") ||
      way.tags.has("horse", "yes")
  }

  private def motorboatAccessible(way: Way): Boolean = {
    way.tags.has("waterway")
  }

  private def canoeAccessible(way: Way): Boolean = {
    way.tags.has("waterway") ||
      way.tags.has("canoe", "portage") || // used for places where canoe has to be carried over a dam
      way.tags.has("canoe", "yes")
  }

  private def inlineSkatesAccessible(way: Way): Boolean = {
    way.tags.has("highway") ||
      way.tags.has("route", "ferry") ||
      way.tags.has("inline_skates", "yes")
  }

}
