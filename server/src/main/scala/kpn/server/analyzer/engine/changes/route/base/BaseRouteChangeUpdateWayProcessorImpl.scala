package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.Bounds
import kpn.api.common.ChangeType
import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.diff.WayDiffsInfo
import kpn.api.common.diff.WayInfo
import kpn.api.common.diff.WayUpdate
import kpn.api.common.route.GeometryDiff
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

    val wayDiffsInfo = analyzeWayDiffs(before, after)

    new RouteGeometryAnalyzer().analyze(before, after) match {
      case Some((geometryDiff: GeometryDiff, bounds: Bounds)) =>
        val key = changeSetContext.buildChangeKey(after.id)
        val change = BaseRouteChange(
          _id = key.toId,
          key = key,
          changeType = ChangeType.Update,
          wayDiffsInfo,
          Some(geometryDiff),
          Some(bounds)
        )
        changeSetContext.copy(
          changes = changeSetContext.changes.copy(
            baseRouteChanges = changeSetContext.changes.baseRouteChanges :+ change
          )
        )
      case None =>
        if (wayDiffsInfo.nonEmpty) {
          val key = changeSetContext.buildChangeKey(after.id)
          val change = BaseRouteChange(
            _id = key.toId,
            key = key,
            changeType = ChangeType.Update,
            wayDiffsInfo,
            None,
            None
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
  }

  private def analyzeWayDiffs(before: Relation, after: Relation): Option[WayDiffsInfo] = {

    val wayIdsBefore = before.ways.map(_.id).toSet
    val wayIdsAfter = after.ways.map(_.id).toSet
    val wayIdsCommon = wayIdsBefore intersect wayIdsAfter

    val removed = toWayInfos(before, wayIdsBefore -- wayIdsAfter)
    val added = toWayInfos(after, wayIdsAfter -- wayIdsBefore)
    val updated = analyzeUpdatedWays(before, after, wayIdsCommon)

    if (removed.nonEmpty || added.nonEmpty || updated.nonEmpty) {
      Some(WayDiffsInfo(removed, added, updated))
    }
    else {
      None
    }
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

  private def toWayInfos(relation: Relation, wayIds: Set[Long]): Seq[WayInfo] = {
    wayIds.toSeq.flatMap { wayId =>
      relation.ways.find(_.id == wayId).map(WayInfo.from)
    }
  }
}
