package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.PreconditionMissingException

object ExpectedNameRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new ExpectedNameRouteAnalyzer(context).analyze
  }
}

class ExpectedNameRouteAnalyzer(context: RouteAnalysisContext) {

  private val routeNameAnalysis = context.routeNameAnalysis.getOrElse(throw new PreconditionMissingException)
  private val routeNodeAnalysis = context.routeNodeAnalysis.getOrElse(throw new PreconditionMissingException)

  def analyze: RouteAnalysisContext = {
    if (canDetermineRouteNameFromNodeNames) {
      val name = routeNameAnalysis.name.get
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
    routeNameAnalysis.name.isDefined &&
      ((routeNodeAnalysis.startNodes.nonEmpty && routeNodeAnalysis.endNodes.nonEmpty) ||
        routeNodeAnalysis.freeNodes.nonEmpty)
  }
}
