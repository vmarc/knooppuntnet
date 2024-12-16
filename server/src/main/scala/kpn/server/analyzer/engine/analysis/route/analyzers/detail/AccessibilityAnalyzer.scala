package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.api.common.NetworkType
import kpn.api.common.data.Way

trait AccessibilityAnalyzer {
  def accessible(networkType: NetworkType, way: Way): Boolean
}
