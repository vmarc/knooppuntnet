package kpn.server.api.analysis.pages.route

import kpn.api.common.Language
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteSegmentsPage
import kpn.api.common.route.SegmentInfo
import kpn.api.common.route.SegmentRouteInfo
import kpn.core.doc.RouteDoc
import kpn.core.util.Util
import kpn.core.util.Util.mergeBounds
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RouteSegmentsPageBuilder(
  routeRepository: RouteRepository,
  changeSetRepository: ChangeSetRepository
) {
  def build(language: Language, routeId: Long): Option[RouteSegmentsPage] = {
    routeRepository.findRouteById(routeId).map { routeDoc =>
      val changeCount = changeSetRepository.routeChangesCount(routeId)
      val segmentCount = routeDoc.segments.size
      val networkReferences = routeRepository.networkReferences(routeId)
      val routeBounds = Util.mergeBounds(routeDoc.segments.map(_.bounds))

      val routeInfo = RouteInfo(
        routeDoc._id,
        routeDoc.base.name,
        routeDoc.base.routeTypes,
        memberCount = routeDoc.structureRows.size,
        pathCount = routeDoc.paths.size,
        segmentCount = routeDoc.segments.size,
        changeCount = changeCount,
        bounds = routeDoc.bounds,
      )

      val segments = buildSegments(routeDoc)

      RouteSegmentsPage(
        routeInfo,
        segments,
      )
    }
  }

  private def buildSegments(routeDoc: RouteDoc): Seq[SegmentInfo] = {
    routeDoc.segments.zipWithIndex.map { case (segment, index) =>
      val meters = segment.meters
      val bounds = Some(segment.bounds)
      val routeInfo = SegmentRouteInfo(
        routeDoc._id,
        Seq(segment.id)
      )
      SegmentInfo(
        index + 1,
        meters,
        bounds,
        Seq(routeInfo)
      )
    }
  }

  private def buildSegments2(routeDoc: RouteDoc): Seq[SegmentInfo] = { // TODO redesign - share with MonitorRouteSegmentsPageBuilder
    routeDoc.superSegments.zipWithIndex.map { case (superSegment, index) =>
      val meters = superSegment.segments.map(_.info.meters).sum
      val bounds = Option.when(superSegment.segments.nonEmpty) {
        mergeBounds(superSegment.segments.map(_.info.bounds))
      }
      val routeInfos = {
        val relationIds = superSegment.segments.map(_.info.relationId).distinct.sorted
        relationIds.map { relationId =>
          val segmentIds = superSegment.segments.filter(_.info.relationId == relationId).map(_.info.segmentId)
          SegmentRouteInfo(
            relationId,
            segmentIds
          )
        }
      }
      SegmentInfo(
        index + 1,
        meters,
        bounds,
        routeInfos
      )
    }
  }
}
