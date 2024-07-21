package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact
import kpn.core.util.Util.isDigits
import kpn.server.analyzer.engine.analysis.route.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object RouteNameFromNodesAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    if (context.nodeNetwork) {
      new RouteNameFromNodesAnalyzer(context).analyze
    }
    else {
      context
    }
  }
}

class RouteNameFromNodesAnalyzer(context: RouteDetailAnalysisContext) {

  private val routeNodeAnalysis = context.nodes

  def analyze: RouteDetailAnalysisContext = {
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

  private def routeNameFromNodeNames(startNodeName: String, endNodeName: String): RouteDetailAnalysisContext = {
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
