package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.data.Way
import kpn.core.analysis.RouteMemberWay
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object RouteStreetsAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new RouteStreetsAnalyzer(context).analyze
  }
}

class RouteStreetsAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
    val ways: Seq[Way] = context.routeMembers.flatMap {
      case w: RouteMemberWay => Some(w.way)
      case _ => None
    }
    val sortedStreets = ways.flatMap(_.tagValue("name")).distinct.sorted
    context.copy(streets = Some(sortedStreets))
  }
}
