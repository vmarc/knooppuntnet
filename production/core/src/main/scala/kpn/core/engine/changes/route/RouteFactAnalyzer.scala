package kpn.core.engine.changes.route

import kpn.core.engine.analysis.route.RouteAnalysis
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.history.RouteTagDiffAnalyzer
import kpn.shared.Fact

class RouteFactAnalyzer(
  analysisData: AnalysisData
) {

  def facts(before: Option[RouteAnalysis], after: RouteAnalysis): Seq[Fact] = {
    Seq(
      test(Fact.LostRouteTags, hasLostRouteTags(before, after)),
      test(Fact.WasOrphan, wasOrphan(after)),
      test(Fact.WasIgnored, wasIgnored(after))
    ).flatten
  }

  private def hasLostRouteTags(before: Option[RouteAnalysis], after: RouteAnalysis): Boolean = {
    before.nonEmpty && hasRouteTags(before.get) && !hasRouteTags(after)
  }

  private def wasOrphan(after: RouteAnalysis) = {
    analysisData.orphanRoutes.watched.contains(after.id)
  }

  private def wasIgnored(after: RouteAnalysis) = {
    analysisData.orphanRoutes.ignored.contains(after.id)
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
      Seq()
    }
  }
}
