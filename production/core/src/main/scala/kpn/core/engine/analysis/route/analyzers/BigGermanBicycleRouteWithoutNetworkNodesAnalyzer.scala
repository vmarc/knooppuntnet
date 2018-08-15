package kpn.core.engine.analysis.route.analyzers

import kpn.core.changes.RelationAnalyzer
import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.Country
import kpn.shared.Fact.IgnoreNotNodeNetwork
import kpn.shared.NetworkType

object BigGermanBicycleRouteWithoutNetworkNodesAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new BigGermanBicycleRouteWithoutNetworkNodesAnalyzer(context).analyze
  }
}

class BigGermanBicycleRouteWithoutNetworkNodesAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    if (isNotNodeNetwork) {
      context.replaceAllFactsWith(IgnoreNotNodeNetwork)
    }
    else {
      context
    }
  }

  private def isNotNodeNetwork: Boolean = {
    context.orphan &&
      isGerman &&
      isBicycleNetwork &&
      isVeryLong &&
      hasNoRouteNodes
  }

  private def isGerman: Boolean = {
    context.loadedRoute.country.contains(Country.de)
  }

  private def isBicycleNetwork: Boolean = {
    context.loadedRoute.networkType == NetworkType.bicycle
  }

  private def isVeryLong: Boolean = {
    val length100km = 100 * 1000
    RelationAnalyzer.waysLength(context.loadedRoute.relation) > length100km
  }

  private def hasNoRouteNodes: Boolean = {
    context.routeNodeAnalysis.get.routeNodes.isEmpty
  }

}
