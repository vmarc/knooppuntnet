package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.api.common.RouteType
import kpn.api.common.data.Way

trait AccessibilityAnalyzer {
  def accessible(role: Option[String], routeType: RouteType, way: Way): Boolean
}
