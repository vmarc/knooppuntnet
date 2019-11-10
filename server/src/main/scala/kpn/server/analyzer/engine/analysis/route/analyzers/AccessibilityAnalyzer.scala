package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.data.Way
import kpn.api.custom.NetworkType

trait AccessibilityAnalyzer {
  def accessible(networkType: NetworkType, way: Way): Boolean
}
