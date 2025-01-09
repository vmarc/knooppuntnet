package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact
import kpn.core.util.Util.isDigits

object BaseRouteNameFromNodesAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    if (context.nodeNetwork) {
      new BaseRouteNameFromNodesAnalyzer(context).analyze
    }
    else {
      context
    }
  }
}

class BaseRouteNameFromNodesAnalyzer(context: BaseRouteAnalysisContext) {

  private val routeNodeAnalysis = context.routeNodesAnalysis

  def analyze: BaseRouteAnalysisContext = {
    if (context.routeNameAnalysis.name.isDefined) {
      context // we already have a route name, no need to try to derive from node names
    }
    else {
      routeNodeAnalysis.startNode.map(_.name) match {
        case None => context // start node name not known, cannot derive route name
        case Some(startNodeName) =>
          routeNodeAnalysis.endNode.map(_.name) match {
            case None => context // end node name not known, cannot derive route name
            case Some(endNodeName) =>
              if (startNodeName.nonEmpty && endNodeName.nonEmpty) {
                routeNameFromNodeNames(startNodeName, endNodeName)
              }
              else {
                context
              }
          }
      }
    }
  }

  private def routeNameFromNodeNames(startNodeName: String, endNodeName: String): BaseRouteAnalysisContext = {
    val separator = if (isDigits(startNodeName) && isDigits(endNodeName)) {
      "-"
    }
    else {
      " - "
    }
    val newRouteNameAnalysis = RouteNameAnalysis(
      name = Some(s"$startNodeName$separator$endNodeName"),
      derivedFromNodes = true
    )
    context.copy(
      facts = context.facts.filterNot(_ == Fact.RouteNameMissing),
      _routeNameAnalysis = Some(newRouteNameAnalysis)
    )
  }
}
