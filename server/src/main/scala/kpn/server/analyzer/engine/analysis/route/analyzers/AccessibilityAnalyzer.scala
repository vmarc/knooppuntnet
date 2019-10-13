package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.shared.NetworkType
import kpn.shared.data.Way

trait AccessibilityAnalyzer {
  def accessible(networkType: NetworkType, way: Way): Boolean
}
