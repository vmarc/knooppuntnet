package kpn.core.engine.analysis.route.analyzers

import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.Fact

object ExpectedNameRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new ExpectedNameRouteAnalyzer(context).analyze
  }
}

class ExpectedNameRouteAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    val routeNameAnalysis = context.routeNameAnalysis.get
    val routeNodeAnalysis = context.routeNodeAnalysis.get
    if (routeNameAnalysis.name.isDefined && routeNodeAnalysis.startNodes.nonEmpty && routeNodeAnalysis.endNodes.nonEmpty) {
      val expectedName = routeNodeAnalysis.startNodes.head.name + "-" + routeNodeAnalysis.endNodes.head.name
      if (!routeNameAnalysis.name.get.equals(expectedName)) {
        context.copy(expectedName = Some(expectedName)).withFact(Fact.RouteNodeNameMismatch)
      }
      else {
        context.copy(expectedName = Some(expectedName))
      }
    }
    else {
      context.copy(expectedName = Some(""))
    }
  }

}
