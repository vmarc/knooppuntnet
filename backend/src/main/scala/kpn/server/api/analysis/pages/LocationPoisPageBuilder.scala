package kpn.server.api.analysis.pages

import kpn.api.common.Language
import kpn.api.common.poi.LocationPoiParameters
import kpn.api.common.poi.LocationPoisPage
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.repository.PoiRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
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
