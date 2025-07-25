package kpn.server.analyzer.engine.monitor.domain

import kpn.api.common.monitor.MonitorRouteDeviation

case class MonitorRouteDeviationAnalysis(
  referenceDistance: Long,
  matchesDistance: Long,
  matchesLines: Seq[String], // reference line segments within tolerance distance of route line segments
  deviations: Seq[MonitorRouteDeviation], // reference line segments NOT within tolerance distance of route line segments
  actualLines: Seq[String], // route line segments NOT within tolerance distance of reference line segments
)
