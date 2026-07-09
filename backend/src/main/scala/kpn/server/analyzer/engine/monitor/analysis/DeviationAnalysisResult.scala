package kpn.server.analyzer.engine.monitor.analysis

import kpn.api.common.monitor.MonitorRouteDeviation

import scala.collection.Seq

case class DeviationAnalysisResult(
  deviations: Seq[MonitorRouteDeviation],
  matchesDistance: Long,
  matchesLines: Seq[String]
)
