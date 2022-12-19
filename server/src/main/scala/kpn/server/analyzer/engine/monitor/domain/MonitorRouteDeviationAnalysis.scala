package kpn.server.analyzer.engine.monitor.domain

import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.server.analyzer.engine.monitor.DeviationAnalysisResult

case class MonitorRouteDeviationAnalysis(
  results: Seq[DeviationAnalysisResult],
  referenceDistance: Long,
  referenceGeometry: String,
  matchesGeometry: Option[String],
  deviations: Seq[MonitorRouteDeviation]
)
