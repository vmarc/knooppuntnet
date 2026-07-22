package kpn.server.analyzer.engine.monitor.analysis

import org.locationtech.jts.geom.LineString

case class MonitorAnalysisResult(
  matchesLines: Seq[LineString],
  deviationLines: Seq[DistanceLineString],
)
