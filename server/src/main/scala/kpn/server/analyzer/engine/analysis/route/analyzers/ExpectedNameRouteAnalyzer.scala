package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.Fact

object ExpectedNameRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new ExpectedNameRouteAnalyzer(context).analyze
  }
}

class ExpectedNameRouteAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    if (routeNameAnalysis.name.isDefined && routeNodeAnalysis.startNodes.nonEmpty && routeNodeAnalysis.endNodes.nonEmpty) {
      val name = routeNameAnalysis.name.get
      val start = routeNodeAnalysis.startNodes.head.name
      val end = routeNodeAnalysis.endNodes.head.name
      val expectedName = start + "-" + end
      val expectedNameReversed = end + "-" + start
      if (name.equals(expectedName) || name.equals(expectedNameReversed)) {
        context.copy(expectedName = Some(expectedName))
      }
      else {
        context.copy(expectedName = Some(expectedName)).withFact(Fact.RouteNodeNameMismatch)
      }
    }
    else {
      context.copy(expectedName = Some(""))
    }
  }

  private def routeNameAnalysis: RouteNameAnalysis = {
    context.routeNameAnalysis.getOrElse(throw new IllegalStateException("RouteNameAnalysis required before expected name analysis"))
  }

  private def routeNodeAnalysis: RouteNodeAnalysis = {
    context.routeNodeAnalysis.getOrElse(throw new IllegalStateException("RouteNodeAnalysis required before expected name analysis"))
  }

}
