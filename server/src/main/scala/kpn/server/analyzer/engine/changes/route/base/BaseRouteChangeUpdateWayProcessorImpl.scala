package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.ChangeType
import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.data.raw.RawWay
import kpn.api.common.diff.WayDiffs
import kpn.api.common.diff.WayUpdate
import kpn.api.custom.Relation
import kpn.core.history.WayDiffAnalyzer
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import org.springframework.stereotype.Component

@Component
class BaseRouteChangeUpdateWayProcessorImpl extends BaseRouteChangeUpdateWayProcessor {

  private val log = Log(classOf[BaseRouteChangeUpdateWayProcessorImpl])

  override def process(
    changeSetContext: ChangeSetContext,
    before: Relation,
    after: Relation,
  ): ChangeSetContext = {

    val wayDiffs = analyzeWayDiffs(before, after)
    if (wayDiffs.nonEmpty) {
      updateChangeSetContext(changeSetContext, after, wayDiffs)
    }
    else {
      changeSetContext
    }
  }

  private def analyzeWayDiffs(before: Relation, after: Relation): WayDiffs = {

    val wayIdsBefore = before.ways.map(_.id).toSet
    val wayIdsAfter = after.ways.map(_.id).toSet
    val wayIdsCommon = wayIdsBefore intersect wayIdsAfter

    val removed = toWays(before, wayIdsBefore -- wayIdsAfter)
    val added = toWays(after, wayIdsAfter -- wayIdsBefore)
    val updated = analyzeUpdatedWays(before, after, wayIdsCommon)

    WayDiffs(
      removed,
      added,
      updated
    )
  }

  private def analyzeUpdatedWays(
    before: Relation,
    after: Relation,
    wayIds: Set[Long]
  ): Seq[WayUpdate] = {
    wayIds.toSeq.sorted.flatMap { wayId =>
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

  private def updateChangeSetContext(changeSetContext: ChangeSetContext, after: Relation, wayDiffs: WayDiffs) = {
    val key = changeSetContext.buildChangeKey(after.id)
    val change = BaseRouteChange(
      _id = key.toId,
      key = key,
      changeType = ChangeType.Update,
      wayDiffs
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
