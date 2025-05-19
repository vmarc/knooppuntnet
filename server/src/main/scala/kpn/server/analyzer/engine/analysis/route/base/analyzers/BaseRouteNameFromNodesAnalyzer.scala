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
    if (isRouteNameAlreadyDefined) {
      context // we already have a route name, no need to try to derive from node names
    }
    else {
      deriveRouteNameFromNodeNames
    }
  }

  private def deriveRouteNameFromNodeNames = {
    getNodeNames match {
      case (Some(startNodeName), Some(endNodeName)) =>
        routeNameFromNodeNames(startNodeName, endNodeName)
      case _ =>
        // cannot derive route name when either start or end node name is not known
        context
    }
  }

  private def getNodeNames: (Option[String], Option[String]) = {
    val startNodeNameOption = context.routeNodesAnalysis.startNode.map(_.name)
    val endNodeNameOption = context.routeNodesAnalysis.endNode.map(_.name)
    (startNodeNameOption, endNodeNameOption)
  }

  private def isRouteNameAlreadyDefined: Boolean = {
    context.routeNameAnalysis.name.isDefined
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
