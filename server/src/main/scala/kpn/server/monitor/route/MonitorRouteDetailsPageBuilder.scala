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

  case class PageBuildContext(
    language: Language,
    isAdmin: Boolean,
    group: MonitorGroup,
    monitorRoute: MonitorRoute,
    routeDetails: Option[RouteDetailsData]
  )

  def build(language: Language, groupName: String, routeName: String): Option[MonitorRouteDetailsPage] = {
    val isAdmin = monitorUserRepository.isAdminUser(RequestContext.user)

    for {
      group <- monitorGroupRepository.groupByName(groupName)
      monitorRoute <- monitorRouteRepository.routeByName(group._id, routeName)
      routeDetails <- monitorRoute.relationId.map(routeRepository.routeDetails)
      context = PageBuildContext(language, isAdmin, group, monitorRoute, routeDetails)
    } yield buildDetailsPage(context)
  }

  private def buildDetailsPage(context: PageBuildContext): MonitorRouteDetailsPage = {
    val routeDetails = buildRouteDetails(context)
    val summary = buildRouteSummary(context, routeDetails)

    MonitorRouteDetailsPage(
      summary,
      context.monitorRoute.comment,
      context.monitorRoute.symbol,
      context.monitorRoute.analysisTimestamp,
      context.monitorRoute.analysisDuration,
      context.monitorRoute.referenceType,
      context.monitorRoute.referenceTimestamp,
      context.monitorRoute.referenceFilename,
      context.monitorRoute.referenceDistance,
      context.monitorRoute.deviationDistance,
      context.monitorRoute.happy,
      routeDetails
    )
  }

  private def buildRouteDetails(context: PageBuildContext): Option[RouteDetails] = {
    context.routeDetails.map { routeDetailsData =>
      val locationCandidateInfos = buildLocationCandidateInfos(context.language, routeDetailsData)
      RouteDetails.from(routeDetailsData, locationCandidateInfos)
    }
  }

  private def buildLocationCandidateInfos(
    language: Language,
    routeDetails: RouteDetailsData
  ): Seq[LocationCandidateInfo] = {
    routeDetails.locationAnalysis.candidates.map { candidate =>
      val locationNames = candidate.location.names
      val locationInfos = locationService.toInfos(language, locationNames, locationNames)
      LocationCandidateInfo(locationInfos, candidate.percentage)
    }
  }

  private def buildRouteSummary(
    context: PageBuildContext,
    routeDetails: Option[RouteDetails]
  ): MonitorRouteSummary = {
    MonitorRouteSummary(
      context.isAdmin,
      context.group.name,
      context.monitorRoute.name,
      context.monitorRoute.description,
      context.monitorRoute._id.oid,
      context.monitorRoute.relationId,
      context.monitorRoute.relationIds,
      routeDetails.map(_.memberCount).getOrElse(0L),
      context.monitorRoute.osmSegmentCount,
      context.monitorRoute.deviationCount,
      context.monitorRoute.bounds
    )
  }
}
