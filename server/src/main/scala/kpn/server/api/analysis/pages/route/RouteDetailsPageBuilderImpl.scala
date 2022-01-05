package kpn.server.api.analysis.pages.route

import kpn.api.common.Language
import kpn.api.common.route.RouteDetailsPage
import kpn.api.common.route.RouteDetailsPageData
import kpn.core.doc.Label
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RouteDetailsPageBuilderImpl(
  routeRepository: RouteRepository,
  changeSetRepository: ChangeSetRepository,
  locationService: LocationService
) extends RouteDetailsPageBuilder {

  override def build(user: Option[String], language: Language, routeId: Long): Option[RouteDetailsPage] = {
    if (routeId == 1) {
      Some(RouteDetailsPageExample.page)
    }
    else {
      doBuildDetailsPage(language, routeId)
    }
  }

  private def doBuildDetailsPage(language: Language, routeId: Long): Option[RouteDetailsPage] = {
    routeRepository.findById(routeId).map { route =>
      val changeCount = changeSetRepository.routeChangesCount(routeId)
      val networkReferences = routeRepository.networkReferences(routeId)

      val routeAnalysis = route.analysis.copy(
        locationAnalysis = locationService.replaceNames(language, route.analysis.locationAnalysis)
      )

      val data = RouteDetailsPageData(
        route._id,
        route.labels.contains(Label.active),
        route.summary,
        route.proposed,
        route.version,
        route.changeSetId,
        route.lastUpdated,
        route.lastSurvey,
        route.tags,
        route.facts,
        routeAnalysis,
        route.tiles,
        route.nodeRefs
      )
      RouteDetailsPage(data, networkReferences, changeCount)
    }
  }
}
