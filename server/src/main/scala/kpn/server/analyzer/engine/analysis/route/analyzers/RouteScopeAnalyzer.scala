package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object RouteScopeAnalyzer extends RouteAnalyzer {
  val localNetworkTagValues = tagValues(NetworkScope.local)
  val regionalNetworkTagValues = tagValues(NetworkScope.regional)
  val nationalNetworkTagValues = tagValues(NetworkScope.national)
  val internationalNetworkTagValues = tagValues(NetworkScope.international)

  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new RouteScopeAnalyzer(context).analyze
  }

  private def tagValues(scope: NetworkScope): Seq[String] = {
    NetworkType.all.map { networkType =>
      ScopedNetworkType(scope, networkType).key
    }
  }
}

class RouteScopeAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
    val scopes: Seq[String] = Tags.get(context.relation.tags, "network") match {
      case None => Seq.empty
      case Some(tagValue) =>
        val values = tagValue.split(";")
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
