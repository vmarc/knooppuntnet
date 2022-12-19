package kpn.server.analyzer.engine.monitor

import kpn.api.common.monitor.MonitorRouteDeviation
import org.locationtech.jts.geom.MultiLineString

import scala.collection.Seq

case class DeviationAnalysisResult(
  deviations: Seq[MonitorRouteDeviation],
  matches: MultiLineString
)
