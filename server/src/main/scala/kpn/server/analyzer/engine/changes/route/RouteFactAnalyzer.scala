package kpn.server.analyzer.engine.changes.route

import kpn.api.common.diff.RouteData
import kpn.api.custom.Fact
import kpn.core.history.RouteTagDiffAnalyzer
import kpn.server.analyzer.engine.context.Watched

class RouteFactAnalyzer(
  analysisData: Watched
) {

  def facts(before: Option[RouteData], after: RouteData): Seq[Fact] = {
    Seq(
      test(Fact.LostRouteTags, hasLostRouteTags(before, after))
    ).flatten
  }

  private def hasLostRouteTags(before: Option[RouteData], after: RouteData): Boolean = {
    before.nonEmpty && hasRouteTags(before.get) && !hasRouteTags(after)
  }

  private def hasRouteTags(routeData: RouteData): Boolean = {
    RouteTagDiffAnalyzer.mainTagKeys.forall(key => routeData.hasTag(key))
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
