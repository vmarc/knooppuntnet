package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.api.common.NetworkScope
import kpn.api.common.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object RouteScopeAnalyzer extends RouteDetailAnalyzer {
  private val localNetworkTagValues = tagValues(NetworkScope.local)
  private val regionalNetworkTagValues = tagValues(NetworkScope.regional)
  private val nationalNetworkTagValues = tagValues(NetworkScope.national)
  private val internationalNetworkTagValues = tagValues(NetworkScope.international)

  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new RouteScopeAnalyzer(context).analyze
  }

  private def tagValues(scope: NetworkScope): Seq[String] = {
    NetworkType.values.map { networkType =>
      ScopedNetworkType(scope, networkType).key
    }
  }
}

class RouteScopeAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
    val scopes: Seq[String] = Tags.get(context.relation.tags, "network") match {
      case None => Seq.empty
      case Some(tagValue) =>
        val values = tagValue.split(";").toSeq
        values.flatMap { value =>
          if (RouteScopeAnalyzer.localNetworkTagValues.contains(value)) {
            Some("local")
          }
          else if (RouteScopeAnalyzer.regionalNetworkTagValues.contains(value)) {
            Some("regional")
          }
          else if (RouteScopeAnalyzer.nationalNetworkTagValues.contains(value)) {
            Some("national")
          }
          else if (RouteScopeAnalyzer.internationalNetworkTagValues.contains(value)) {
            Some("international")
          }
          else {
            None
          }
        }
    }
    val allScopes = if (scopes.isEmpty) {
      Seq("unknown")
    }
    else {
      scopes
    }

    context.copy(_scopes = Some(allScopes))
  }
}
