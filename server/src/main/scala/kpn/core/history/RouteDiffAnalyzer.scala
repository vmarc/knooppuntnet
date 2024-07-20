package kpn.core.history

import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawWay
import kpn.api.common.diff.RouteData
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.WayUpdate
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.diff.route.RouteNameDiff
import kpn.api.common.diff.route.RouteNodeDiff
import kpn.api.custom.Fact
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.structure.RouteAnalysisNode
import kpn.server.analyzer.engine.changes.diff.RouteUpdate

class RouteDiffAnalyzer(before: RouteDetailAnalysisContext, after: RouteDetailAnalysisContext) {

  private val log = Log(classOf[RouteDiffAnalyzer])

  def analysis: RouteUpdate = {

    val diffs = findDiffs

    val facts = if ((after.facts.contains(Fact.RouteTagMissing) && !before.facts.contains(Fact.RouteTagMissing)) ||
      (after.facts.contains(Fact.RouteTagInvalid) && !before.facts.contains(Fact.RouteTagInvalid))) {
      Seq(Fact.LostRouteTags)
    }
    else {
      Seq.empty
    }

    val beforeRouteData = RouteData.from(before)
    val afterRouteData = RouteData.from(after)

    RouteUpdate(
      beforeRouteData,
      afterRouteData,
      removedWays,
      addedWays,
      updatedWays,
      diffs,
      facts
    )
  }

  private def removedWays: Seq[RawWay] = {
    (wayIdsBefore -- wayIdsAfter).toSeq.flatMap { wayId =>
      before.ways.find(_.id == wayId) match {
        case Some(way) => Some(
          RawWay(
            way.id,
            way.version,
            way.timestamp,
            way.changeSetId,
            way.nodes.map(_.id),
            way.tags
          )
        )
        case None =>
          //noinspection SideEffectsInMonadicTransformation
          log.warn(s"inconsistant data: could not find removed way $wayId in before data")
          None
      }
    }
  }

  private def addedWays: Seq[RawWay] = {
    (wayIdsAfter -- wayIdsBefore).toSeq.flatMap { wayId =>
      after.ways.find(_.id == wayId) match {
        case Some(way) => Some(
          RawWay(
            way.id,
            way.version,
            way.timestamp,
            way.changeSetId,
            way.nodes.map(_.id),
            way.tags
          )
        )
        case None =>
          //noinspection SideEffectsInMonadicTransformation
          log.warn(s"inconsistant data: could not find added way $wayId in after data")
          None
      }
    }
  }

  private def updatedWays: Seq[WayUpdate] = {

    wayIdsCommon.toSeq.sorted.flatMap { wayId =>

      val wayBeforeOption = before.ways.find(_.id == wayId)
      val wayAfterOption = after.ways.find(_.id == wayId)

      if (wayBeforeOption.isEmpty) {
        //noinspection SideEffectsInMonadicTransformation
        log.warn(s"inconsistant data: could not find way $wayId in before data")
        None
      } else if (wayAfterOption.isEmpty) {
        //noinspection SideEffectsInMonadicTransformation
        log.warn(s"inconsistant data: could not find way $wayId in after data")
        None
      }
      else {
        val wayBefore = wayBeforeOption.get
        val wayAfter = wayAfterOption.get
        new WayDiffAnalyzer(wayBefore, wayAfter).analysis
      }
    }
  }

  private def findDiffs: RouteDiff = {
    RouteDiff(
      nameDiff,
      None, // role differences can only be seen in the context of a network
      factDiffs,
      nodeDiffs,
      memberOrderChanged,
      tagDiffs
    )
  }

  private def factDiffs: Option[FactDiffs] = {

    val beforeFacts = before.facts.toSet
    val afterFacts = after.facts.toSet

    val resolvedFacts = (beforeFacts -- afterFacts).toSeq
    val introducedFacts = (afterFacts -- beforeFacts).toSeq
    val remainingFacts = (afterFacts intersect beforeFacts).toSeq

    if (resolvedFacts.nonEmpty || introducedFacts.nonEmpty) {
      Some(
        FactDiffs(
          resolvedFacts,
          introducedFacts,
          remainingFacts
        ))
    }
    else {
      None
    }
  }

  private def nameDiff: Option[RouteNameDiff] = {

    val nameBefore = before.routeNameAnalysis.name.getOrElse("no-name")
    val nameAfter = after.routeNameAnalysis.name.getOrElse("no-name")

    if (nameBefore != nameAfter) {
      Some(RouteNameDiff(nameBefore, nameAfter))
    }
    else {
      None
    }
  }

  private def nodeDiffs: Seq[RouteNodeDiff] = {
    Seq(
      nodeChanged("startNodes", before.nodes.startNode.toSeq, after.nodes.startNode.toSeq),
      nodeChanged("endNodes", before.nodes.endNode.toSeq, after.nodes.endNode.toSeq),
      nodeChanged("startTentacleNodes", before.nodes.startTentacleNodes, after.nodes.startTentacleNodes),
      nodeChanged("endTentacleNodes", before.nodes.endTentacleNodes, after.nodes.endTentacleNodes)
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

    val beforeMembers = before.routeMembers.map(_.id)
    val afterMembers = after.routeMembers.map(_.id)
    beforeMembers != afterMembers && beforeMembers.toSet == afterMembers.toSet
  }

  private def tagDiffs: Option[TagDiffs] = {
    new RouteTagDiffAnalyzer(before.relation, after.relation).diffs
  }

  private def nodeChanged(title: String, before: Seq[RouteAnalysisNode], after: Seq[RouteAnalysisNode]): Option[RouteNodeDiff] = {

    val beforeNodeIds = before.map(_.node.id).toSet
    val afterNodeIds = after.map(_.node.id).toSet

    if (beforeNodeIds == afterNodeIds) {
      None
    }
    else {
      val added = afterNodeIds -- beforeNodeIds
      val removed = beforeNodeIds -- afterNodeIds

      val addedNodeRefs = after.filter(n => added.contains(n.node.id)).map(n => Ref(n.node.id, n.name))
      val removedNodeRefs = before.filter(n => removed.contains(n.node.id)).map(n => Ref(n.node.id, n.name))

      Some(RouteNodeDiff(title, addedNodeRefs, removedNodeRefs))
    }
  }

  private def wayIdsBefore: Set[Long] = before.ways.map(_.id).toSet

  private def wayIdsAfter: Set[Long] = after.ways.map(_.id).toSet

  private def wayIdsCommon: Set[Long] = wayIdsBefore intersect wayIdsAfter
}
