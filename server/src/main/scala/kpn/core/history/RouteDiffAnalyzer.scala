package kpn.core.history

import kpn.api.common.Fact
import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.diff.RouteData
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.diff.RouteUpdate

class RouteDiffAnalyzer(before: RouteData, after: RouteData, baseRouteChangeOption: Option[BaseRouteChange]) {

  private val log = Log(classOf[RouteDiffAnalyzer])

  def analysis: RouteUpdate = {

    val facts = if ((after.facts.contains(Fact.RouteTagMissing) && !before.facts.contains(Fact.RouteTagMissing)) ||
      (after.facts.contains(Fact.RouteTagInvalid) && !before.facts.contains(Fact.RouteTagInvalid))) {
      Seq(Fact.LostRouteTags)
    }
    else {
      Seq.empty
    }

    RouteUpdate(
      before,
      after,
      RouteDiff.empty,
      facts
    )
  }

  private def analyzeFactDiffs(): Option[FactDiffs] = {

    val beforeFacts = before.facts.toSet
    val afterFacts = after.facts.toSet

    val resolvedFacts = (beforeFacts -- afterFacts).toSeq
    val introducedFacts = (afterFacts -- beforeFacts).toSeq
    val remainingFacts = (afterFacts intersect beforeFacts).toSeq

    Option.when(resolvedFacts.nonEmpty || introducedFacts.nonEmpty) {
      FactDiffs(
        resolvedFacts,
        introducedFacts,
        remainingFacts
      )
    }
  }
}
