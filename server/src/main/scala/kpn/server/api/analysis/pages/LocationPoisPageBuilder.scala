package kpn.server.api.analysis.pages

import kpn.api.common.Language
import kpn.api.common.poi.LocationPoiParameters
import kpn.api.common.poi.LocationPoisPage
import kpn.api.custom.LocationKey
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.repository.PoiRepository
import org.springframework.stereotype.Component

@Component
class LocationPoisPageBuilder(
  poiRepository: PoiRepository,
  locationService: LocationService
) {

  def build(
    language: Language,
    locationKeyParam: LocationKey,
    parameters: LocationPoiParameters
  ): LocationPoisPage = {
    val locationKey = locationService.translate(language, locationKeyParam)
    val poiCount = poiRepository.locationPoiCount(locationKey.name)
    val pois = poiRepository.locationPois(locationKey.name, parameters)
    LocationPoisPage(
      TimeInfoBuilder.timeInfo,
      poiCount,
      pois
    )
  }
}
