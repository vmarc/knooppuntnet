package kpn.server.analyzer.engine.monitor

import kpn.api.common.monitor.MonitorRouteDeviation

import scala.collection.Seq

case class DeviationAnalysisResult(
  deviations: Seq[MonitorRouteDeviation],
  matchesDistance: Long,
  matchesLines: Seq[String]
)
