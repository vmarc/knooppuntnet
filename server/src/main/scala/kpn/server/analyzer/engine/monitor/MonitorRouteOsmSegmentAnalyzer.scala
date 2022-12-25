package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.WayMember
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteOsmSegmentAnalysis

trait MonitorRouteOsmSegmentAnalyzer {
  def analyze(wayMembers: Seq[WayMember]): MonitorRouteOsmSegmentAnalysis
}
