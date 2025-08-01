package kpn.server.monitor.route

import kpn.api.common.Language
import kpn.api.common.location.LocationCandidateInfo
import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.api.common.monitor.MonitorRouteSummary
import kpn.api.common.route.RouteDetails
import kpn.database.actions.routes.RouteDetailsData
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.config.RequestContext
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorUserRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteDetailsPageBuilder(
  routeRepository: RouteRepository,
  monitorUserRepository: MonitorUserRepository,
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
  locationService: LocationService
) {

  def build(language: Language, groupName: String, routeName: String): Option[MonitorRouteDetailsPage] = {
    val adminUser = monitorUserRepository.isAdminUser(RequestContext.user)
    monitorGroupRepository.groupByName(groupName).flatMap { group =>
      monitorRouteRepository.routeByName(group._id, routeName).map { monitorRoute =>
        val routeDetailsOption = monitorRoute.relationId.flatMap(routeRepository.routeDetails)
        buildPage(
          language,
          adminUser,
          group,
          monitorRoute,
          routeDetailsOption
        )
      }
    }
  }

  private def buildPage(
    language: Language,
    adminUser: Boolean,
    group: MonitorGroup,
    monitorRoute: MonitorRoute,
    routeDetailsOption: Option[RouteDetailsData]
  ): MonitorRouteDetailsPage = {

    val details = routeDetailsOption.map { routeDetails =>
      val locationCandidateInfos = routeDetails.locationAnalysis.candidates.map { candidate =>
        val locationNames = candidate.location.names
        val locationInfos = locationService.toInfos(language, locationNames, locationNames)
        LocationCandidateInfo(locationInfos, candidate.percentage)
      }
      RouteDetails.from(routeDetails, locationCandidateInfos)
    }

    val summary = MonitorRouteSummary(
      adminUser,
      group.name,
      monitorRoute.name,
      monitorRoute.description,
      monitorRoute._id.oid,
      monitorRoute.relationId,
      monitorRoute.relationIds,
      details.map(_.memberCount).getOrElse(0L),
      monitorRoute.osmSegmentCount,
      monitorRoute.deviationCount,
      monitorRoute.bounds,
    )

    MonitorRouteDetailsPage(
      summary,
      monitorRoute.comment,
      monitorRoute.symbol,
      monitorRoute.analysisTimestamp,
      monitorRoute.analysisDuration,
      monitorRoute.referenceType,
      monitorRoute.referenceTimestamp,
      monitorRoute.referenceFilename,
      monitorRoute.referenceDistance,
      monitorRoute.deviationDistance,
      monitorRoute.happy,
      details,
    )
  }
}
