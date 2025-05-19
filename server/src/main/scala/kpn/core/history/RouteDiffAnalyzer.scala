package kpn.core.history

import kpn.api.common.Fact
import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.common.Ref
import kpn.api.common.diff.RouteData
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.WayDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.diff.route.RouteNameDiff
import kpn.api.common.diff.route.RouteNodeDiff
import kpn.api.common.route.RouteNode
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.diff.RouteUpdate

class RouteDiffAnalyzer(before: RouteData, after: RouteData, baseRouteChangeOption: Option[BaseRouteChange]) {

  private val log = Log(classOf[RouteDiffAnalyzer])

  def analysis: RouteUpdate = {

    val diffs = analyzeDiffs()

    val facts = if ((after.facts.contains(Fact.RouteTagMissing) && !before.facts.contains(Fact.RouteTagMissing)) ||
      (after.facts.contains(Fact.RouteTagInvalid) && !before.facts.contains(Fact.RouteTagInvalid))) {
      Seq(Fact.LostRouteTags)
    }
    else {
      Seq.empty
    }

    val wayDiffs = baseRouteChangeOption match {
      case Some(baseRouteChange) => baseRouteChange.wayDiffs
      case None => WayDiffs.empty
    }

    RouteUpdate(
      before,
      after,
      wayDiffs,
      diffs,
      facts
    )
  }

  private def analyzeDiffs(): RouteDiff = {
    RouteDiff(
      analyzeNameDiff(),
      None, // role differences can only be seen in the context of a network
      analyzeFactDiffs(),
      analyzeNodeDiffs(),
      memberOrderChanged,
      tagDiffs()
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

  private def analyzeNameDiff(): Option[RouteNameDiff] = {

    val nameBefore = before.name
    val nameAfter = after.name

    if (nameBefore != nameAfter) {
      Some(RouteNameDiff(nameBefore, nameAfter))
    }
    else {
      None
    }
  }

  private def analyzeNodeDiffs(): Seq[RouteNodeDiff] = {
    Seq(
      nodeChanged("node", before.networkNodes, after.networkNodes),
      //  nodeChanged("endNodes", before.nodes.endNode.toSeq, after.nodes.endNode.toSeq),
      //  nodeChanged("startTentacleNodes", before.nodes.startTentacleNodes, after.nodes.startTentacleNodes),
      //  nodeChanged("endTentacleNodes", before.nodes.endTentacleNodes, after.nodes.endTentacleNodes)
      // TODO redesign - should include 'redundantNodes'?
    ).flatten
  }

  private def memberOrderChanged: Boolean = {
    /*
      TODO CHANGE The old implementation did take 'unexpected' members (such as relations) into account. Current implementation good enough?

        val beforeRelation = beforeSnapshot.data.relations(routeId)
        val afterRelation = afterSnapshot.data.relations(routeId)
        beforeRelation.members != afterRelation.members && beforeRelation.members.toSet == afterRelation.members.toSet
     */

    //  val beforeMembers = before.routeMembers.map(_.id)
    //  val afterMembers = after.routeMembers.map(_.id)
    //  beforeMembers != afterMembers && beforeMembers.toSet == afterMembers.toSet
    false // TODO redesign - add members to RouteData
  }

  private def tagDiffs(): Option[TagDiffs] = {
    new RouteTagDiffAnalyzer(before, after).diffs
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
