package kpn.server.api.analysis.pages.location

import kpn.api.common.Language
import kpn.api.common.NetworkType
import kpn.api.common.location.LocationFactsPage
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.repository.LocationRepository
import org.springframework.stereotype.Component

@Component
class LocationFactsPageBuilderImpl(
  locationRepository: LocationRepository,
  locationService: LocationService
) extends LocationFactsPageBuilder {

  override def build(language: Language, locationKey: LocationKey): Option[LocationFactsPage] = {
    if (locationKey == LocationKey(NetworkType.cycling, Country.nl, "example")) {
      Some(LocationFactsPageExample.page)
    }
    else {
      buildPage(language, locationKey)
    }
  }

  private def buildPage(language: Language, locationKeyParam: LocationKey): Option[LocationFactsPage] = {
    val subset = locationService.toSubset(language, locationKeyParam)
    val summary = locationRepository.summary(subset)
    val locationFacts = locationRepository.facts(subset)
    Some(
      LocationFactsPage(summary, locationFacts)
    )
  }
}
