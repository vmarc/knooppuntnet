package kpn.server.monitor.route

import kpn.api.common.Language
import kpn.api.common.monitor.MonitorRouteSegmentsPage
import kpn.api.common.monitor.MonitorRouteSummary
import kpn.core.doc.RouteDoc
import kpn.server.config.RequestContext
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorUserRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
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
          buildPage(language, adminUser, group, monitorRoute, routeDoc)
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
  ): MonitorRouteSegmentsPage = {

    val summary = MonitorRouteSummary(
      adminUser,
      group.name,
      monitorRoute.name,
      monitorRoute.description,
      monitorRoute._id.oid,
      monitorRoute.relationId,
      monitorRoute.relationIds,
      memberCount = routeDoc.structureRows.length,
      segmentCount = monitorRoute.osmSegmentCount,
      deviationCount = monitorRoute.deviationCount,
      monitorRoute.bounds,
    )

    MonitorRouteSegmentsPage(
      summary,
      routeDoc.summary.meters,
      routeDoc.segments,
      routeDoc.superDistance,
      // TODO redesign - remove temporary code to assign super segment ids
      routeDoc.superSegments.zipWithIndex.map { case (segment, index) => segment.copy(id = index + 1) }
    )
  }
}
