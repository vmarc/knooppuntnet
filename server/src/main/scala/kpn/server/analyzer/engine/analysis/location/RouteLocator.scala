package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.RouteLocationAnalysis
import kpn.server.analyzer.engine.analysis.route.structure.RouteAnalysisSegment

trait RouteLocator {
  def locate(segments: Seq[RouteAnalysisSegment]): RouteLocationAnalysis
}
