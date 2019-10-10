package kpn.core.history

import kpn.core.analysis.NetworkMemberRoute
import kpn.core.engine.changes.diff.RouteUpdate
import kpn.core.util.Log
import kpn.shared.Fact
import kpn.shared.common.Ref
import kpn.shared.data.raw.RawWay
import kpn.shared.diff.TagDiffs
import kpn.shared.diff.WayUpdate
import kpn.shared.diff.common.FactDiffs
import kpn.shared.diff.route.RouteDiff
import kpn.shared.diff.route.RouteNameDiff
import kpn.shared.diff.route.RouteNodeDiff
import kpn.shared.diff.route.RouteRoleDiff
import kpn.shared.route.RouteNetworkNodeInfo

class NetworkRouteDiffAnalyzer(beforeSnapshot: NetworkSnapshot, afterSnapshot: NetworkSnapshot, routeId: Long) {

  private val log = Log(classOf[NetworkRouteDiffAnalyzer])

  private val before: NetworkMemberRoute = routeBefore(routeId).get
  // TODO need to make more safe?
  private val after: NetworkMemberRoute = routeAfter(routeId).get

  private def beforeRouteRelation = beforeSnapshot.data.relations(routeId)

  private def afterRouteRelation = afterSnapshot.data.relations(routeId)


  private def wayIdsBefore: Set[Long] = beforeRouteRelation.wayMembers.map(_.way.id).toSet

  private def wayIdsAfter: Set[Long] = afterRouteRelation.wayMembers.map(_.way.id).toSet

  private def wayIdsCommon: Set[Long] = wayIdsBefore intersect wayIdsAfter

  def analysis: Option[RouteUpdate] = {

    val diffs = findDiffs

    val facts = if ((after.routeAnalysis.route.facts.contains(Fact.RouteTagMissing) && !before.routeAnalysis.route.facts.contains(Fact.RouteTagMissing)) ||
      (after.routeAnalysis.route.facts.contains(Fact.RouteTagInvalid) && !before.routeAnalysis.route.facts.contains(Fact.RouteTagInvalid))) {
      Seq(Fact.LostRouteTags)
    }
    else {
      Seq()
    }

    val update = RouteUpdate(
      before.routeAnalysis,
      after.routeAnalysis,
      removedWays,
      addedWays,
      updatedWays,
      diffs,
      facts
    )

    if (update.nonEmpty) Some(update) else None
  }

  private def removedWays: Seq[RawWay] = {
    (wayIdsBefore -- wayIdsAfter).toSeq.flatMap { wayId =>
      beforeSnapshot.data.ways.get(wayId) match {
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
      afterSnapshot.data.ways.get(wayId) match {
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

      val wayBeforeOption = beforeSnapshot.data.ways.get(wayId)
      val wayAfterOption = afterSnapshot.data.ways.get(wayId)

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
      roleDiff,
      factDiffs,
      nodeDiffs,
      memberOrderChanged,
      tagDiffs
    )
  }

  private def factDiffs: Option[FactDiffs] = {

    val beforeFacts = before.routeAnalysis.route.facts.toSet
    val afterFacts = after.routeAnalysis.route.facts.toSet

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

    val nameBefore = before.routeAnalysis.route.summary.name
    val nameAfter = after.routeAnalysis.route.summary.name

    if (nameBefore != nameAfter) {
      Some(RouteNameDiff(nameBefore, nameAfter))
    }
    else {
      None
    }
  }

  private def roleDiff: Option[RouteRoleDiff] = {
    if (before.role != after.role) {
      Some(RouteRoleDiff(before.role, after.role))
    }
    else {
      None
    }
  }

  private def nodeDiffs: Seq[RouteNodeDiff] = {
    val ba = before.routeAnalysis
    val aa = after.routeAnalysis
    Seq(
      nodeChanged("startNodes", ba.startNodes, aa.startNodes),
      nodeChanged("endNodes", ba.endNodes, aa.endNodes),
      nodeChanged("startTentacleNodes", ba.startTentacleNodes, aa.startTentacleNodes),
      nodeChanged("endTentacleNodes", ba.endTentacleNodes, aa.endTentacleNodes)
    ).flatten
  }

  private def memberOrderChanged: Boolean = {
    val beforeRelation = beforeSnapshot.data.relations(routeId)
    val afterRelation = afterSnapshot.data.relations(routeId)
    beforeRelation.members != afterRelation.members && beforeRelation.members.toSet == afterRelation.members.toSet
  }

  private def tagDiffs: Option[TagDiffs] = {
    val beforeRelation = beforeSnapshot.data.relations(routeId)
    val afterRelation = afterSnapshot.data.relations(routeId)
    new RouteTagDiffAnalyzer(beforeRelation, afterRelation).diffs
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

  private def routeAfter(id: Long): Option[NetworkMemberRoute] = {
    afterSnapshot.network.routes.find(_.routeAnalysis.route.id == id)
  }

  private def routeBefore(id: Long): Option[NetworkMemberRoute] = {
    beforeSnapshot.network.routes.find(_.routeAnalysis.route.id == id)
  }

}
