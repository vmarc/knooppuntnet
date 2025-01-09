package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact

object BaseRouteExpectedNameAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    if (context.nodeNetwork) {
      new BaseRouteExpectedNameAnalyzer(context).analyze
    }
    else {
      context
    }
  }
}

class BaseRouteExpectedNameAnalyzer(context: BaseRouteAnalysisContext) {

  private val routeNodeAnalysis = context.routeNodesAnalysis

  def analyze: BaseRouteAnalysisContext = {
    if (canDetermineRouteNameFromNodeNames) {
      val name = context.routeNameAnalysis.name.get
      val start = routeNodeAnalysis.startNode.map(_.name).getOrElse("")
      val end = routeNodeAnalysis.endNode.map(_.name).getOrElse("")
      val separator = if (name.contains(" - ")) " - " else "-"
      val expectedName = s"$start$separator$end"
      val expectedNameReversed = s"$end$separator$start"
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
    context.routeNameAnalysis.name.nonEmpty &&
      routeNodeAnalysis.startNode.nonEmpty &&
      routeNodeAnalysis.endNode.nonEmpty
  }
}
