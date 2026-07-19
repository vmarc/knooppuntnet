package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.ChangeType
import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.diff.route.RouteDiff
import kpn.core.history.RouteTagDiffAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.changes.ChangeSetContext
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class BaseRouteDiffAnalyzer(
  baseRouteDiffNameAnalyzer: BaseRouteDiffNameAnalyzer,
  baseRouteDiffFactsAnalyzer: BaseRouteDiffFactsAnalyzer,
  baseRouteDiffNodesAnalyzer: BaseRouteDiffNodesAnalyzer,
  baseRouteDiffMemberAnalyzer: BaseRouteDiffMemberAnalyzer,
  baseRouteDiffGeometryAnalyzer: BaseRouteDiffGeometryAnalyzer,
  baseRouteDiffWaysAnalyzer: BaseRouteDiffWaysAnalyzer
) {

  def analyze(
    changeSetContext: ChangeSetContext,
    before: BaseRouteAnalysisContext,
    after: BaseRouteAnalysisContext
  ): ChangeSetContext = {

    val routeDiff = analyzeDiffs(before, after)

    val geometryDiff = baseRouteDiffGeometryAnalyzer.analyze(before.relation, after.relation)
    val wayDiffsInfo = baseRouteDiffWaysAnalyzer.analyze(before, after)

    if (routeDiff.nonEmpty || geometryDiff.nonEmpty || wayDiffsInfo.nonEmpty) {
      val key = changeSetContext.buildChangeKey(after.relation.id)
      val change = BaseRouteChange(
        _id = key.toId,
        key = key,
        changeType = ChangeType.Update,
        before = Some(before.relation.toMeta),
        after = Some(after.relation.toMeta),
        routeDiff,
        wayDiffsInfo,
        geometryDiff
      )
      changeSetContext.copy(
        changes = changeSetContext.changes.copy(
          baseRouteChanges = changeSetContext.changes.baseRouteChanges :+ change
        )
      )
    }
    else {
      changeSetContext
    }
  }

  private def analyzeDiffs(before: BaseRouteAnalysisContext, after: BaseRouteAnalysisContext): RouteDiff = {
    RouteDiff(
      baseRouteDiffNameAnalyzer.analyze(before, after),
      None, // role differences can only be seen in the context of a network
      baseRouteDiffFactsAnalyzer.analyze(before, after),
      baseRouteDiffNodesAnalyzer.analyze(before, after),
      baseRouteDiffMemberAnalyzer.memberOrderChanged(before, after),
      new RouteTagDiffAnalyzer(before.relation, after.relation).diffs
    )
  }
}
