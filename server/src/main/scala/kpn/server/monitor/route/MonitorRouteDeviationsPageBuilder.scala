package kpn.server.monitor.route

import kpn.api.common.Language
import kpn.api.common.monitor.MonitorRouteDeviationInfo
import kpn.api.common.monitor.MonitorRouteDeviationsPage
import kpn.api.common.monitor.MonitorRouteSummary
import kpn.server.config.RequestContext
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorUserRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteDeviationsPageBuilder(
  monitorUserRepository: MonitorUserRepository,
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
) {

  def build(language: Language, groupName: String, routeName: String): Option[MonitorRouteDeviationsPage] = {
    val adminUser = monitorUserRepository.isAdminUser(RequestContext.user)
    monitorGroupRepository.groupByName(groupName).flatMap { group =>
      monitorRouteRepository.routeByName(group._id, routeName).flatMap { monitorRoute =>
        monitorRoute.relationId.map(relationId => monitorRouteRepository.routeMemberCount(relationId)).map { memberCount =>
          val deviations = monitorRouteRepository.routeDeviations(monitorRoute._id)
          buildPage(
            language,
            adminUser,
            group,
            monitorRoute,
            memberCount,
            deviations
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
    memberCount: Long,
    deviations: Seq[MonitorRouteDeviationInfo]
  ): MonitorRouteDeviationsPage = {

    val deviationDistance = deviations.map(_.distance).sum

    val summary = MonitorRouteSummary(
      adminUser,
      group.name,
      monitorRoute.name,
      monitorRoute.description,
      monitorRoute._id.toHexString,
      monitorRoute.relationId,
      monitorRoute.relationIds,
      memberCount,
      monitorRoute.osmSegmentCount,
      deviations.length,
      monitorRoute.bounds,
    )

    MonitorRouteDeviationsPage(
      summary,
      deviationDistance,
      deviations
    )
  }
}
