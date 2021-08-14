package kpn.server.analyzer.engine.changes.route

import kpn.api.custom.Fact
import kpn.core.history.RouteTagDiffAnalyzer
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.context.Watched

class RouteFactAnalyzer(
  analysisData: Watched
) {

  def facts(before: Option[RouteAnalysis], after: RouteAnalysis): Seq[Fact] = {
    Seq(
      test(Fact.LostRouteTags, hasLostRouteTags(before, after)),
      test(Fact.WasOrphan, wasOrphan(after))
    ).flatten
  }

  private def hasLostRouteTags(before: Option[RouteAnalysis], after: RouteAnalysis): Boolean = {
    before.nonEmpty && hasRouteTags(before.get) && !hasRouteTags(after)
  }

  private def wasOrphan(after: RouteAnalysis) = {
    analysisData.routes.contains(after.id)
  }

  private def hasRouteTags(routeAnalysis: RouteAnalysis): Boolean = {
    val tags = routeAnalysis.route.tags
    RouteTagDiffAnalyzer.mainTagKeys.forall(key => tags.has(key))
  }

  private def test(fact: Fact, exists: Boolean): Seq[Fact] = {
    if (exists) {
      Seq(fact)
    }
    else {
      Seq.empty
    }
  }
}
