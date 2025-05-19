package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.ChangeType
import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.data.raw.RawWay
import kpn.api.common.diff.WayUpdate
import kpn.api.custom.Relation
import kpn.core.history.WayDiffAnalyzer
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext

class BaseRouteChangeUpdateWayProcessorImpl extends BaseRouteChangeUpdateWayProcessor {

  private val log = Log(classOf[BaseRouteChangeUpdateWayProcessorImpl])

  override def process(
    changeSetContext: ChangeSetContext,
    before: Relation,
    after: Relation,
  ): ChangeSetContext = {

    val wayIdsBefore = before.ways.map(_.id).toSet
    val wayIdsAfter = after.ways.map(_.id).toSet
    val wayIdsCommon = wayIdsBefore intersect wayIdsAfter

    val removedWays = toWays(before, wayIdsBefore -- wayIdsAfter)
    val addedWays = toWays(after, wayIdsAfter -- wayIdsBefore)
    val updatedWays = analyzeUpdatedWays(before, after, wayIdsCommon)

    if (removedWays.nonEmpty || addedWays.nonEmpty || updatedWays.nonEmpty) {
      updateChangeSetContext(changeSetContext, after, removedWays, addedWays, updatedWays)
    }
    else {
      changeSetContext
    }
  }

  private def analyzeUpdatedWays(
    before: Relation,
    after: Relation,
    wayIdsCommon: Set[Long]
  ): Seq[WayUpdate] = {
    wayIdsCommon.toSeq.sorted.flatMap { wayId =>
      val wayBeforeOption = before.ways.find(_.id == wayId)
      val wayAfterOption = after.ways.find(_.id == wayId)
      (wayBeforeOption, wayAfterOption) match {
        case (Some(wayBefore), Some(wayAfter)) =>
          new WayDiffAnalyzer(wayBefore, wayAfter).analysis
        case _ =>
          Seq.empty
      }
    }
  }

  private def updateChangeSetContext(changeSetContext: ChangeSetContext, after: Relation, removedWays: Seq[RawWay], addedWays: Seq[RawWay], updatedWays: Seq[WayUpdate]) = {
    val key = changeSetContext.buildChangeKey(after.id)
    val change = BaseRouteChange(
      _id = key.toId,
      key = key,
      changeType = ChangeType.Update,
      removedWays,
      addedWays,
      updatedWays,
    )
    changeSetContext.copy(
      changes = changeSetContext.changes.copy(
        baseRouteChanges = changeSetContext.changes.baseRouteChanges :+ change
      )
    )
  }

  private def toWays(relation: Relation, wayIds: Set[Long]): Seq[RawWay] = {
    wayIds.toSeq.flatMap { wayId =>
      relation.ways.find(_.id == wayId).map(_.toRaw)
    }
  }
}
