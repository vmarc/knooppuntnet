package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.NetworkScope
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Tags

object BaseRouteScopeAnalyzer extends BaseRouteAnalyzer {
  private val localNetworkTagValues = tagValues(NetworkScope.local)
  private val regionalNetworkTagValues = tagValues(NetworkScope.regional)
  private val nationalNetworkTagValues = tagValues(NetworkScope.national)
  private val internationalNetworkTagValues = tagValues(NetworkScope.international)

  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteScopeAnalyzer(context).analyze
  }

  private def tagValues(scope: NetworkScope): Seq[String] = {
    RouteType.values.map { routeType =>
      ScopedRouteType(scope, routeType).key
    }
  }
}

class BaseRouteScopeAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {
    val scopes: Seq[RouteScope] = Tags.get(context.relation.tags, "network") match {
      case None => Seq.empty
      case Some(tagValue) =>
        val values = tagValue.split(";").toSeq
        values.flatMap { value =>
          if (BaseRouteScopeAnalyzer.localNetworkTagValues.contains(value)) {
            Some(RouteScope.Local)
          }
          else if (BaseRouteScopeAnalyzer.regionalNetworkTagValues.contains(value)) {
            Some(RouteScope.Regional)
          }
          else if (BaseRouteScopeAnalyzer.nationalNetworkTagValues.contains(value)) {
            Some(RouteScope.National)
          }
          else if (BaseRouteScopeAnalyzer.internationalNetworkTagValues.contains(value)) {
            Some(RouteScope.International)
          }
          else {
            None
          }
        }
    }
    val allScopes = if (scopes.isEmpty) {
      Seq(RouteScope.Unknown)
    }
    else {
      scopes
    }

    context.copy(_scopes = Some(allScopes))
  }
}
