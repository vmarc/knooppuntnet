package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.common.Ref
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.diff.route.RouteNameDiff
import kpn.api.common.diff.route.RouteNodeDiff
import kpn.api.common.route.RouteNode
import kpn.core.history.RouteTagDiffAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import org.springframework.stereotype.Component

@Component
class BaseRouteDiffAnalyzer {

  def analyze(before: BaseRouteAnalysisContext, after: BaseRouteAnalysisContext): RouteDiff = {
    val diffs = analyzeDiffs(before, after)


    // TODO !!!
    //    val lostRouteTags = TagInterpreter.isRouteRelation(relationBefore) &&
    //      !TagInterpreter.isRouteRelation(relationAfter)

    //    val facts = if ((after.facts.contains(Fact.RouteTagMissing) && !before.facts.contains(Fact.RouteTagMissing)) ||
    //      (after.facts.contains(Fact.RouteTagInvalid) && !before.facts.contains(Fact.RouteTagInvalid))) {
    //      Seq(Fact.LostRouteTags)
    //    }
    //    else {
    //      Seq.empty
    //    }
    //
    //    RouteUpdate(
    //      before,
    //      after,
    //      diffs,
    //      facts
    //    )
    diffs
  }

  private def analyzeDiffs(before: BaseRouteAnalysisContext, after: BaseRouteAnalysisContext): RouteDiff = {
    RouteDiff(
      analyzeNameDiff(before, after),
      None, // role differences can only be seen in the context of a network
      analyzeFactDiffs(before, after),
      analyzeNodeDiffs(before, after),
      memberOrderChanged(before, after),
      tagDiffs(before, after)
    )
  }

  private def analyzeFactDiffs(before: BaseRouteAnalysisContext, after: BaseRouteAnalysisContext): Option[FactDiffs] = {

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

  private def analyzeNameDiff(before: BaseRouteAnalysisContext, after: BaseRouteAnalysisContext): Option[RouteNameDiff] = {
    val nameBefore = before.routeNameAnalysis.name
    val nameAfter = after.routeNameAnalysis.name
    if (nameBefore != nameAfter) {
      Some(RouteNameDiff(nameBefore, nameAfter))
    }
    else {
      None
    }
  }

  private def analyzeNodeDiffs(before: BaseRouteAnalysisContext, after: BaseRouteAnalysisContext): Seq[RouteNodeDiff] = {

    val beforeNodes = before.routeNodesAnalysis.nodes.map(_.toRouteNode)
    val afterNodes = after.routeNodesAnalysis.nodes.map(_.toRouteNode)

    Seq(
      nodeChanged("node", beforeNodes, afterNodes),
      //  nodeChanged("endNodes", before.nodes.endNode.toSeq, after.nodes.endNode.toSeq),
      //  nodeChanged("startTentacleNodes", before.nodes.startTentacleNodes, after.nodes.startTentacleNodes),
      //  nodeChanged("endTentacleNodes", before.nodes.endTentacleNodes, after.nodes.endTentacleNodes)
      // TODO redesign - should include 'redundantNodes'?
    ).flatten
  }

  private def memberOrderChanged(before: BaseRouteAnalysisContext, after: BaseRouteAnalysisContext): Boolean = {
    val beforeMembers = before.routeMembers
    val afterMembers = after.routeMembers

    if (beforeMembers.sizeIs == afterMembers.sizeIs) {
      false
    }
    else {
      val beforeMemberIds = beforeMembers.map(member => member.memberType -> member.id)
      val afterMemberIds = beforeMembers.map(member => member.memberType -> member.id)
      beforeMemberIds != afterMemberIds && beforeMemberIds.toSet == afterMemberIds.toSet
    }
  }

  private def tagDiffs(before: BaseRouteAnalysisContext, after: BaseRouteAnalysisContext): Option[TagDiffs] = {
    new RouteTagDiffAnalyzer(before.relation, after.relation).diffs
  }

  private def nodeChanged(title: String, before: Seq[RouteNode], after: Seq[RouteNode]): Option[RouteNodeDiff] = {

    val beforeNodeIds = before.map(_.nodeId).toSet
    val afterNodeIds = after.map(_.nodeId).toSet

    if (beforeNodeIds == afterNodeIds) {
      None
    }
    else {
      val added = afterNodeIds -- beforeNodeIds
      val removed = beforeNodeIds -- afterNodeIds

      val addedNodeRefs = after.filter(n => added.contains(n.nodeId)).map(n => Ref(n.nodeId, n.name))
      val removedNodeRefs = before.filter(n => removed.contains(n.nodeId)).map(n => Ref(n.nodeId, n.name))

      Some(RouteNodeDiff(title, addedNodeRefs, removedNodeRefs))
    }
  }
}
