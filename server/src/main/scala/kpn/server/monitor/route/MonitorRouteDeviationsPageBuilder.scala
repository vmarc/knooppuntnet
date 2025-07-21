package kpn.server.monitor.route

import kpn.api.common.Language
import kpn.api.common.monitor.MonitorRouteDeviationInfo
import kpn.api.common.monitor.MonitorRouteDeviationsPage
import kpn.api.common.monitor.MonitorRouteSummary
import kpn.core.doc.RouteDoc
import kpn.server.config.RequestContext
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorUserRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteDeviationsPageBuilder(
  routeRepository: RouteRepository,
  monitorUserRepository: MonitorUserRepository,
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
) {

  def build(language: Language, groupName: String, routeName: String): Option[MonitorRouteDeviationsPage] = {
    val adminUser = monitorUserRepository.isAdminUser(RequestContext.user)
    monitorGroupRepository.groupByName(groupName).flatMap { group =>
      monitorRouteRepository.routeByName(group._id, routeName).flatMap { monitorRoute =>
        monitorRoute.relationId.flatMap(routeRepository.findRouteById).map { routeDoc =>
          val states = monitorRouteRepository.states(monitorRoute._id) // TODO limit query to only the info that is needed: deviationCount, deviationDistance
          buildPage(language, adminUser, group, monitorRoute, routeDoc, states)
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
    states: Seq[MonitorState]
  ): MonitorRouteDeviationsPage = {

    val deviations = states.flatMap {
      _.deviations.map { deviation =>
        MonitorRouteDeviationInfo(
          deviation.id,
          deviation.meters,
          deviation.distance,
          deviation.bounds,
        )
      }
    }

    val sorted = deviations.sortBy(_.meters).reverse.zipWithIndex.map { case (deviation, index) => deviation.copy(id = index + 1) }

    val deviationDistance = deviations.map(_.distance).sum

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
      deviationCount = deviations.length,
      monitorRoute.bounds,
    )

    MonitorRouteDeviationsPage(
      summary,
      deviationDistance,
      sorted
    )
  }
}
