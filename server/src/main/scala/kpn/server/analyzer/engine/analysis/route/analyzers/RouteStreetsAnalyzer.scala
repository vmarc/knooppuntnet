package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.core.analysis.RouteMemberWay
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.data.Way

object RouteStreetsAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteStreetsAnalyzer(context).analyze
  }
}

class RouteStreetsAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    val ways: Seq[Way] = context.routeMembers.get.flatMap {
      case w: RouteMemberWay => Some(w.way)
      case _ => None
    }
    val sortedStreets = ways.flatMap(_.tags("name")).distinct.sorted
    context.copy(streets = Some(sortedStreets))
  }

}
