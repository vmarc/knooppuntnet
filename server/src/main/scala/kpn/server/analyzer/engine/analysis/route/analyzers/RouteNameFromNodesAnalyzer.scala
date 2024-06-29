package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact
import kpn.core.util.Util.isDigits
import kpn.server.analyzer.engine.analysis.route.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object RouteNameFromNodesAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    if (context.nodeNetwork) {
      new RouteNameFromNodesAnalyzer(context).analyze
    }
    else {
      context
    }
  }
}

class RouteNameFromNodesAnalyzer(context: RouteAnalysisContext) {

  private val routeNodeAnalysis = context.oldRouteNodeAnalysis

  def analyze: RouteAnalysisContext = {
    if (context.routeNameAnalysis.name.isDefined) {
      context // we already have a route name, no need to try to derive from node names
    }
    else {
      routeNodeAnalysis.startNodes.map(_.name).headOption match {
        case None => context // start node name not known, cannot derive route name
        case Some(startNodeName) =>
          routeNodeAnalysis.endNodes.map(_.name).headOption match {
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

  private def routeNameFromNodeNames(startNodeName: String, endNodeName: String): RouteAnalysisContext = {
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
      oldFacts = context.oldFacts.filterNot(_ == Fact.RouteNameMissing),
      _routeNameAnalysis = Some(newRouteNameAnalysis)
    )
  }
}
