package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.RouteLocationAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisSegment

trait RouteLocator {
  def locate(segments: Seq[RouteAnalysisSegment]): RouteLocationAnalysis
}
