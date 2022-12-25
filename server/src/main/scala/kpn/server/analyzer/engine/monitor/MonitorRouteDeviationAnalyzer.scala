package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.Way
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteDeviationAnalysis

trait MonitorRouteDeviationAnalyzer {
  def analyze(ways: Seq[Way], referenceGeometry: String): MonitorRouteDeviationAnalysis
}
