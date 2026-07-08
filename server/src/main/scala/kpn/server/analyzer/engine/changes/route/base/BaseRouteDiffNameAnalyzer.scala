package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.diff.route.RouteNameDiff
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class BaseRouteDiffNameAnalyzer {

  def analyze(before: BaseRouteAnalysisContext, after: BaseRouteAnalysisContext): Option[RouteNameDiff] = {
    val nameBefore = before.routeNameAnalysis.name
    val nameAfter = after.routeNameAnalysis.name
    if (nameBefore != nameAfter) {
      Some(RouteNameDiff(nameBefore, nameAfter))
    }
    else {
      None
    }
  }
}
