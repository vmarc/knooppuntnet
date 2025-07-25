package kpn.server.analyzer.engine.monitor.analysis

import kpn.server.analyzer.engine.monitor.domain.MonitorRouteDeviationAnalysis
import org.locationtech.jts.geom.LineString

trait MonitorRouteDeviationAnalyzer {
  def analyze(routeLines: Seq[LineString], referenceLines: Seq[LineString]): MonitorRouteDeviationAnalysis
}
