package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Tags

object BaseRouteScopeAnalyzer extends BaseRouteAnalyzer {
  private val localNetworkTagValues = tagValues(RouteScope.local)
  private val regionalNetworkTagValues = tagValues(RouteScope.regional)
  private val nationalNetworkTagValues = tagValues(RouteScope.national)
  private val internationalNetworkTagValues = tagValues(RouteScope.international)

  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteScopeAnalyzer(context).analyze
  }

  private def tagValues(scope: RouteScope): Seq[String] = {
    RouteType.values.toSeq.map { routeType =>
      ScopedRouteType(routeType, scope).key
    }
  }
}

class BaseRouteScopeAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {
    val scopes: Seq[RouteScope] = Tags.values(context.relation.tags, "network").flatMap { value =>
      if (BaseRouteScopeAnalyzer.localNetworkTagValues.contains(value)) {
        Some(RouteScope.local)
      }
      else if (BaseRouteScopeAnalyzer.regionalNetworkTagValues.contains(value)) {
        Some(RouteScope.regional)
      }
      else if (BaseRouteScopeAnalyzer.nationalNetworkTagValues.contains(value)) {
        Some(RouteScope.national)
      }
      else if (BaseRouteScopeAnalyzer.internationalNetworkTagValues.contains(value)) {
        Some(RouteScope.international)
      }
      else {
        None
      }
    }
    val allScopes = if (scopes.isEmpty) {
      Seq(RouteScope.unknown)
    }
    else {
      scopes
    }

    context.copy(_scopes = Some(allScopes))
  }
}
