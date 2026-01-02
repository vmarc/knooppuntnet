package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.data.Way
import kpn.api.common.diff.WayDiffsInfo
import kpn.api.common.diff.WayInfo
import kpn.api.common.diff.WayUpdate
import kpn.core.history.WayDiffAnalyzer
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import org.springframework.stereotype.Component

@Component
class BaseRouteChangeUpdateWayProcessor {

  private val log = Log(classOf[BaseRouteChangeUpdateWayProcessor])

  def process(
    before: BaseRouteAnalysisContext,
    after: BaseRouteAnalysisContext
  ): Option[WayDiffsInfo] = {
    analyzeWayDiffs(before.relation.ways, after.relation.ways)
  }

  private def analyzeWayDiffs(before: Seq[Way], after: Seq[Way]): Option[WayDiffsInfo] = {

    val wayIdsBefore = before.map(_.id).toSet
    val wayIdsAfter = after.map(_.id).toSet
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
    before: Seq[Way],
    after: Seq[Way],
    wayIds: Set[Long]
  ): Seq[WayUpdate] = {
    wayIds.toSeq.sorted.flatMap { wayId =>
      val wayBeforeOption = before.find(_.id == wayId)
      val wayAfterOption = after.find(_.id == wayId)
      (wayBeforeOption, wayAfterOption) match {
        case (Some(wayBefore), Some(wayAfter)) =>
          new WayDiffAnalyzer(wayBefore, wayAfter).analysis
        case _ =>
          Seq.empty
      }
    }
  }

  private def toWayInfos(ways: Seq[Way], wayIds: Set[Long]): Seq[WayInfo] = {
    wayIds.toSeq.flatMap { wayId =>
      ways.find(_.id == wayId).map(WayInfo.from)
    }
  }
}
