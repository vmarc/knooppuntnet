package kpn.core.engine.analysis.route.analyzers

import kpn.core.changes.RelationAnalyzer
import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.Country
import kpn.shared.Fact.IgnoreNotNodeNetwork
import kpn.shared.NetworkType

object BigGermanBicycleRouteWithoutNameRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new BigGermanBicycleRouteWithoutNameRouteAnalyzer(context).analyze
  }
}

class BigGermanBicycleRouteWithoutNameRouteAnalyzer(context: RouteAnalysisContext) {

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
      hasNoNoteTag
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

  private def hasNoNoteTag: Boolean = {
    !context.loadedRoute.relation.tags.has("note")
  }

}
