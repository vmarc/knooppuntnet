package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteSegmentInfo
import kpn.server.monitor.domain.MonitorRouteOsmSegment
import kpn.server.monitor.MonitorUtil
import org.springframework.stereotype.Component

@Component
class MonitorRouteGapAnalyzer {

  def calculate(
    monitorRouteSegmentInfos: Seq[MonitorRouteSegmentInfo],
    superRouteSuperSegments: Seq[MonitorRouteOsmSegment],
    monitorRouteRelation: MonitorRouteRelation
  ): MonitorRouteRelation = {

    val relationIds = MonitorUtil.subRelationsInRouteRelation(monitorRouteRelation).map(_.relationId)

    val relationWithSegmentsIds = relationIds.filter { relationId =>
      monitorRouteSegmentInfos.exists(_.relationId == relationId)
    }

    val gapInfos = relationWithSegmentsIds.zipWithIndex.map { case (relationId, index) =>

      var gaps: Seq[String] = Seq.empty

      if (index == 0) {
        gaps = gaps :+ "start"
      }
      else {
        val previousRelationId = relationWithSegmentsIds(index - 1)
        if (!isConnecting(monitorRouteSegmentInfos, previousRelationId, relationId)) {
          gaps = gaps :+ "top"
        }
      }

      val osmSegmentCount = monitorRouteSegmentInfos.count(_.relationId == relationId)

      if (osmSegmentCount > 1) {
        gaps = gaps :+ "middle"
      }

      if (index == relationWithSegmentsIds.size - 1) {
        gaps = gaps :+ "end"
      }
      else {
        val nextRelationId = relationWithSegmentsIds(index + 1)
        if (!isConnecting(monitorRouteSegmentInfos, relationId, nextRelationId)) {
          gaps = gaps :+ "bottom"
        }
      }

      MonitorRouteRelationGapInfo(
        relationId = relationId,
        osmSegmentCount = osmSegmentCount,
        startNodeId = 0,
        endNodeId = 0,
        gaps = Some(if (gaps.isEmpty) "" else gaps.mkString("-"))
      )
    }

    updatedMonitorRouteRelationGap(monitorRouteRelation, gapInfos)
  }

  private def isConnecting(monitorRouteSegmentInfos: Seq[MonitorRouteSegmentInfo], relationId1: Long, relationId2: Long): Boolean = {
    val segments1 = monitorRouteSegmentInfos.filter(_.relationId == relationId1)
    val segments2 = monitorRouteSegmentInfos.find(_.relationId == relationId2)
    segments1.exists { segment1 =>
      segments2.exists { segment2 =>
        segment1.startNodeId == segment2.startNodeId ||
          segment1.startNodeId == segment2.endNodeId ||
          segment1.endNodeId == segment2.startNodeId ||
          segment1.endNodeId == segment2.endNodeId
      }
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
