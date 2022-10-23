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
    location: String,
    parameters: LocationPoiParameters,
    layers: Seq[String]
  ): LocationPoisPage = {
    val locationId = locationService.toId(language, location)
    val poiCount = poiRepository.locationPoiCount(locationId, layers)
    val pois = poiRepository.locationPois(locationId, parameters, layers)
    LocationPoisPage(
      poiCount,
      pois
    )
  }
}
