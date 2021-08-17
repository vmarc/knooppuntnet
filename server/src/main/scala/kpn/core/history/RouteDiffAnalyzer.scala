package kpn.core.history

import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawWay
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.WayUpdate
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.diff.route.RouteNameDiff
import kpn.api.common.diff.route.RouteNodeDiff
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.custom.Fact
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.changes.diff.RouteUpdate

class RouteDiffAnalyzer(before: RouteAnalysis, after: RouteAnalysis) {

  private val log = Log(classOf[RouteDiffAnalyzer])

  def analysis: RouteUpdate = {

    val diffs = findDiffs

    val facts = if ((after.route.facts.contains(Fact.RouteTagMissing) && !before.route.facts.contains(Fact.RouteTagMissing)) ||
      (after.route.facts.contains(Fact.RouteTagInvalid) && !before.route.facts.contains(Fact.RouteTagInvalid))) {
      Seq(Fact.LostRouteTags)
    }
    else {
      Seq.empty
    }

    RouteUpdate(
      before,
      after,
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
        case Some(way) => Some(way.raw)
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
        case Some(way) => Some(way.raw)
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

    val beforeFacts = before.route.facts.toSet
    val afterFacts = after.route.facts.toSet

    val resolvedFacts = beforeFacts -- afterFacts
    val introducedFacts = afterFacts -- beforeFacts
    val remainingFacts = afterFacts intersect beforeFacts

    if (resolvedFacts.nonEmpty || introducedFacts.nonEmpty) {
      Some(FactDiffs(resolvedFacts, introducedFacts, remainingFacts))
    }
    else {
      None
    }
  }

  private def nameDiff: Option[RouteNameDiff] = {

    val nameBefore = before.route.summary.name
    val nameAfter = after.route.summary.name

    if (nameBefore != nameAfter) {
      Some(RouteNameDiff(nameBefore, nameAfter))
    }
    else {
      None
    }
  }

  private def nodeDiffs: Seq[RouteNodeDiff] = {
    Seq(
      nodeChanged("startNodes", before.startNodes, after.startNodes),
      nodeChanged("endNodes", before.endNodes, after.endNodes),
      nodeChanged("startTentacleNodes", before.startTentacleNodes, after.startTentacleNodes),
      nodeChanged("endTentacleNodes", before.endTentacleNodes, after.endTentacleNodes)
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
    new RouteTagDiffAnalyzer(before.route, after.route).diffs
  }

  private def nodeChanged(title: String, before: Seq[RouteNetworkNodeInfo], after: Seq[RouteNetworkNodeInfo]): Option[RouteNodeDiff] = {

    val beforeNodeIds = before.map(_.id).toSet
    val afterNodeIds = after.map(_.id).toSet

    if (beforeNodeIds == afterNodeIds) {
      None
    }
    else {
      val added = afterNodeIds -- beforeNodeIds
      val removed = beforeNodeIds -- afterNodeIds

      val addedNodeRefs = after.filter(n => added.contains(n.id)).map(n => Ref(n.id, n.name))
      val removedNodeRefs = before.filter(n => removed.contains(n.id)).map(n => Ref(n.id, n.name))

      Some(RouteNodeDiff(title, addedNodeRefs, removedNodeRefs))
    }
  }

  private def wayIdsBefore: Set[Long] = before.ways.map(_.id).toSet

  private def wayIdsAfter: Set[Long] = after.ways.map(_.id).toSet

  private def wayIdsCommon: Set[Long] = wayIdsBefore intersect wayIdsAfter

}
