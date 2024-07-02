package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object ExpectedNameRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    if (context.nodeNetwork) {
      new ExpectedNameRouteAnalyzer(context).analyze
    }
    else {
      context
    }
  }
}

class ExpectedNameRouteAnalyzer(context: RouteDetailAnalysisContext) {

  private val routeNodeAnalysis = context.oldRouteNodeAnalysis

  def analyze: RouteDetailAnalysisContext = {
    if (canDetermineRouteNameFromNodeNames) {
      val name = context.routeNameAnalysis.name.get
      val start = if (routeNodeAnalysis.freeNodes.isEmpty) {
        routeNodeAnalysis.startNodes.head.name
      }
      else {
        routeNodeAnalysis.freeNodes.head.name
      }
      val end = if (routeNodeAnalysis.freeNodes.isEmpty) {
        routeNodeAnalysis.endNodes.head.name
      }
      else {
        routeNodeAnalysis.freeNodes.head.name
      }
      val separator = if (name.contains(" - ")) " - " else "-"
      val expectedName = start + separator + end
      val expectedNameReversed = end + separator + start
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

  private def canDetermineRouteNameFromNodeNames: Boolean = {
    context.routeNameAnalysis.name.isDefined &&
      ((routeNodeAnalysis.startNodes.nonEmpty && routeNodeAnalysis.endNodes.nonEmpty) ||
        routeNodeAnalysis.freeNodes.nonEmpty)
  }
}
