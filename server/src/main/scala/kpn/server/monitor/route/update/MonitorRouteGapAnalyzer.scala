package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteSegmentInfo
import kpn.server.monitor.domain.MonitorRouteOsmSegment
import org.springframework.stereotype.Component

@Component
class MonitorRouteGapAnalyzer {

  def calculate(
    monitorRouteSegmentInfos: Seq[MonitorRouteSegmentInfo],
    superRouteSuperSegments: Seq[MonitorRouteOsmSegment],
    monitorRouteRelation: MonitorRouteRelation
  ): MonitorRouteRelation = {
    val gapInfos = collectMonitorRouteRelationGapInfos(
      monitorRouteRelation,
      superRouteSuperSegments,
      monitorRouteSegmentInfos
    )
    val calculatedGapInfos = calculateGaps(gapInfos)
    updatedMonitorRouteRelationGap(monitorRouteRelation, calculatedGapInfos)
  }

  private def collectMonitorRouteRelationGapInfos(
    monitorRouteRelation: MonitorRouteRelation,
    superRouteSegments: Seq[MonitorRouteOsmSegment],
    segments: Seq[MonitorRouteSegmentInfo]
  ): Seq[MonitorRouteRelationGapInfo] = {
    val endNodes: Seq[(Long, Long)] = superRouteSegments.flatMap(_.elements).flatMap { element =>
      if (element.relationId == monitorRouteRelation.relationId) {
        segments.find(_.relationId == element.relationId) match {
          case None => None
          case Some(segment) =>
            if (element.reversed) {
              Some((segment.endNodeId, segment.startNodeId))
            }
            else {
              Some((segment.startNodeId, segment.endNodeId))
            }
        }
      }
      else {
        None
      }
    }

    val gapInfos: Seq[MonitorRouteRelationGapInfo] = if (endNodes.isEmpty) {
      Seq.empty
    }
    else {
      val startNodeId = endNodes.headOption.map(_._1).getOrElse(0L)
      val endNodeId = endNodes.lastOption.map(_._2).getOrElse(0L)
      Seq(
        MonitorRouteRelationGapInfo(
          monitorRouteRelation.relationId,
          monitorRouteRelation.osmSegmentCount,
          startNodeId,
          endNodeId,
          None
        )
      )
    }

    gapInfos ++ monitorRouteRelation.relations.flatMap { r =>
      collectMonitorRouteRelationGapInfos(
        r,
        superRouteSegments,
        segments
      )
    }
  }

  private def calculateGaps(gapInfos: Seq[MonitorRouteRelationGapInfo]): Seq[MonitorRouteRelationGapInfo] = {

    gapInfos.zipWithIndex.map { case (gapInfo, index) =>
      var gaps: Seq[String] = Seq.empty

      if (index == 0) {
        gaps = gaps :+ "start"
      }
      else {
        val previousGapInfo = gapInfos(index - 1)
        if (previousGapInfo.endNodeId != gapInfo.startNodeId) {
          gaps = gaps :+ "top"
        }
      }

      if (gapInfo.osmSegmentCount > 1) {
        gaps = gaps :+ "middle"
      }

      if (index == gapInfos.size - 1) {
        gaps = gaps :+ "end"
      }
      else {
        val nextGapInfo = gapInfos(index + 1)
        if (gapInfo.endNodeId != nextGapInfo.startNodeId) {
          gaps = gaps :+ "bottom"
        }
      }

      gapInfo.copy(
        gaps = Some(if (gaps.isEmpty) "" else gaps.mkString("-"))
      )
    }
  }

  private def updatedMonitorRouteRelationGap(monitorRouteRelation: MonitorRouteRelation, gapInfos: Seq[MonitorRouteRelationGapInfo]): MonitorRouteRelation = {
    val updatedRelations = monitorRouteRelation.relations.map(r => updatedMonitorRouteRelationGap(r, gapInfos))
    val gaps = gapInfos.find(_.relationId == monitorRouteRelation.relationId).flatMap(_.gaps)
    monitorRouteRelation.copy(
      gaps = gaps,
      relations = updatedRelations
    )
  }
}
