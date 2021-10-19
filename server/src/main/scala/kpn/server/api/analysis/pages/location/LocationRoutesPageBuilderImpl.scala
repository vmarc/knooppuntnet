package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationRoutesPage
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.LocationRoutesType
import kpn.api.custom.NetworkType
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.LocationRepository
import org.springframework.stereotype.Component

@Component
class LocationRoutesPageBuilderImpl(locationRepository: LocationRepository) extends LocationRoutesPageBuilder {

  override def build(locationKey: LocationKey, parameters: LocationRoutesParameters): Option[LocationRoutesPage] = {
    if (locationKey == LocationKey(NetworkType.cycling, Country.nl, "example")) {
      Some(LocationRoutesPageExample.page)
    }
    else {
      buildPage(locationKey, parameters)
    }
  }

  private def buildPage(locationKey: LocationKey, parameters: LocationRoutesParameters): Option[LocationRoutesPage] = {

    val summary = locationRepository.summary(locationKey)
    val routes = locationRepository.routes(locationKey, parameters)

    val allRouteCount = locationRepository.routeCount(locationKey, LocationRoutesType.all)
    val factsRouteCount = locationRepository.routeCount(locationKey, LocationRoutesType.facts)
    val inaccessibleRouteCount = locationRepository.routeCount(locationKey, LocationRoutesType.inaccessible)
    val surveyRouteCount = locationRepository.routeCount(locationKey, LocationRoutesType.survey)
    val routeCount = parameters.locationRoutesType match {
      case LocationRoutesType.all => allRouteCount
      case LocationRoutesType.facts => factsRouteCount
      case LocationRoutesType.inaccessible => inaccessibleRouteCount
      case LocationRoutesType.survey => surveyRouteCount
      case _ => 0
    }

    Some(
      LocationRoutesPage(
        TimeInfoBuilder.timeInfo,
        summary,
        routeCount,
        allRouteCount,
        factsRouteCount,
        inaccessibleRouteCount,
        surveyRouteCount,
        routes
      )
    )
  }
}
