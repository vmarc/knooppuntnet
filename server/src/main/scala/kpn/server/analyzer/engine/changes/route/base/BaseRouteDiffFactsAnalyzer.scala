package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.diff.common.FactDiffs
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import org.springframework.stereotype.Component

@Component
class BaseRouteDiffFactsAnalyzer {

  def analyze(before: BaseRouteAnalysisContext, after: BaseRouteAnalysisContext): Option[FactDiffs] = {

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
