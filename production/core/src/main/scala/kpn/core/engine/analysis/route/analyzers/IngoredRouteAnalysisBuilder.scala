package kpn.core.engine.analysis.route.analyzers

import kpn.core.changes.RelationAnalyzer
import kpn.core.engine.analysis.route.RouteAnalysis
import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.RouteSummary
import kpn.shared.route.RouteInfo

class IngoredRouteAnalysisBuilder {

  def build(context: RouteAnalysisContext): RouteAnalysis = {

    val summary = RouteSummary(
      context.loadedRoute.relation.id,
      context.loadedRoute.country,
      context.loadedRoute.networkType,
      context.loadedRoute.name,
      RelationAnalyzer.waysLength(context.loadedRoute.relation), // TODO ROUTE move to common class?
      isBroken = false,
      context.loadedRoute.relation.wayMembers.size,
      context.loadedRoute.relation.timestamp,
      Seq(),
      context.loadedRoute.relation.tags
    )

    val lastUpdated = RelationAnalyzer.lastUpdated(context.loadedRoute.relation) // TODO ROUTE move to common class?

    val route = RouteInfo(
      summary,
      active = true,
      display = false,
      ignored = true,
      orphan = true,
      context.loadedRoute.relation.version,
      context.loadedRoute.relation.changeSetId,
      lastUpdated,
      context.loadedRoute.relation.tags,
      context.facts,
      None
    )

    RouteAnalysis(
      context.loadedRoute.relation,
      route = route
    )
  }

}
