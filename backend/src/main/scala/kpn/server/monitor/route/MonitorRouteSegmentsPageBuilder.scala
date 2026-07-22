package kpn.server.monitor.route

import kpn.api.common.Bounds
import kpn.api.common.Language
import kpn.api.common.monitor.MonitorRouteRelationInfo
import kpn.api.common.monitor.MonitorRouteSegmentsPage
import kpn.api.common.monitor.MonitorRouteSummary
import kpn.api.common.route.SegmentInfo
import kpn.api.common.route.SegmentRouteInfo
import kpn.core.doc.RouteDoc
import kpn.server.config.RequestContext
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorUserRepository
import kpn.server.repository.RouteRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class MonitorRouteSegmentsPageBuilder(
  routeRepository: RouteRepository,
  monitorUserRepository: MonitorUserRepository,
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
) {

  def build(language: Language, groupName: String, routeName: String): Option[MonitorRouteSegmentsPage] = {
    val adminUser = monitorUserRepository.isAdminUser(RequestContext.user)
    monitorGroupRepository.groupByName(groupName).flatMap { group =>
      monitorRouteRepository.routeByName(group._id, routeName).flatMap { monitorRoute =>
        monitorRoute.relationId.flatMap(routeRepository.findRouteById).map { routeDoc =>
          val routeRelationInfos = monitorRouteRepository.routeRelationInfos(routeDoc.routeIds)
          buildPage(
            language,
            adminUser,
            group,
            monitorRoute,
            routeDoc,
            routeRelationInfos
          )
        }
      }
    }
  }

  private def buildPage(
    language: Language,
    adminUser: Boolean,
    group: MonitorGroup,
    monitorRoute: MonitorRoute,
    routeDoc: RouteDoc,
    routeRelationInfos: Seq[MonitorRouteRelationInfo]
  ): MonitorRouteSegmentsPage = {

    val summary = buildSummary(adminUser, group, monitorRoute, routeDoc)
    val segments = buildSegments(routeDoc)

    MonitorRouteSegmentsPage(
      summary,
      routeDoc.base.meters,
      routeRelationInfos,
      segments
    )
  }

  private def buildSummary(adminUser: Boolean, group: MonitorGroup, monitorRoute: MonitorRoute, routeDoc: RouteDoc): MonitorRouteSummary = {
    MonitorRouteSummary(
      adminUser,
      group.name,
      monitorRoute.name,
      monitorRoute.description,
      monitorRoute._id.toHexString,
      monitorRoute.relationId,
      monitorRoute.relationIds,
      memberCount = routeDoc.structureRows.length,
      segmentCount = monitorRoute.osmSegmentCount,
      deviationCount = monitorRoute.deviationCount,
      monitorRoute.bounds,
    )
  }

  private def buildSegments(routeDoc: RouteDoc): Seq[SegmentInfo] = {
    routeDoc.superSegments.zipWithIndex.map { case (superSegment, index) =>
      val meters = superSegment.segments.map(_.info.meters).sum
      val bounds = Option.when(superSegment.segments.nonEmpty) {
        Bounds.merge(superSegment.segments.map(_.info.bounds))
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
