package kpn.server.analyzer.engine.changes.route

import kpn.api.custom.Fact
import kpn.core.history.RouteTagDiffAnalyzer
import kpn.server.analyzer.engine.analysis.route.RouteDetailAnalysis
import kpn.server.analyzer.engine.context.Watched

class RouteFactAnalyzer(
  analysisData: Watched
) {

  def facts(before: Option[RouteDetailAnalysis], after: RouteDetailAnalysis): Seq[Fact] = {
    Seq(
      test(Fact.LostRouteTags, hasLostRouteTags(before, after))
    ).flatten
  }

  private def hasLostRouteTags(before: Option[RouteDetailAnalysis], after: RouteDetailAnalysis): Boolean = {
    before.nonEmpty && hasRouteTags(before.get) && !hasRouteTags(after)
  }

  private def hasRouteTags(routeAnalysis: RouteDetailAnalysis): Boolean = {
    RouteTagDiffAnalyzer.mainTagKeys.forall(key => routeAnalysis.relation.hasTag(key))
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
