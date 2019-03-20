package kpn.core.engine.analysis.route.analyzers

import kpn.core.engine.analysis.route.domain.RouteAnalysisContext

object RouteStreetsAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteStreetsAnalyzer(context).analyze
  }
}

class RouteStreetsAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    val sortedStreets = context.ways.toSeq.flatten.flatMap(_.tags("name")).distinct.sorted
    context.copy(streets = Some(sortedStreets))
  }

}
