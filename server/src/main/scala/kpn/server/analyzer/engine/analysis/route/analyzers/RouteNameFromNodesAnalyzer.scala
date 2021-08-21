package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact
import kpn.core.util.Util.isDigits
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.{RouteNameAnalysis, RouteNodeAnalysis}

object RouteNameFromNodesAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteNameFromNodesAnalyzer(context).analyze
  }
}

class RouteNameFromNodesAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    if (routeNameAnalysis.name.isDefined) {
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
    val routeNameAnalysis = RouteNameAnalysis(
      name = Some(s"$startNodeName$separator$endNodeName"),
      derivedFromNodes = true
    )
    context.copy(
      facts = context.facts.filterNot(_ == Fact.RouteNameMissing),
      routeNameAnalysis = Some(routeNameAnalysis)
    )
  }

  private def routeNameAnalysis: RouteNameAnalysis = {
    context.routeNameAnalysis.getOrElse(
      throw new IllegalStateException("RouteNameAnalysis required before route name from nodes analysis")
    )
  }

  private def routeNodeAnalysis: RouteNodeAnalysis = {
    context.routeNodeAnalysis.getOrElse(
      throw new IllegalStateException("RouteNodeAnalysis required before route name from nodes analysis")
    )
  }
}
