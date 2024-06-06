package kpn.server.api.analysis.pages.location

import kpn.api.common.Language
import kpn.api.common.LocationInfo
import kpn.api.common.location.LocationDetailsPage
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.repository.LocationRepository
import org.springframework.stereotype.Component

@Component
class LocationDetailsPageBuilderImpl(
  locationRepository: LocationRepository,
  locationService: LocationService
) extends LocationDetailsPageBuilder {

  override def build(language: Language, locationKey: LocationKey): Option[LocationDetailsPage] = {
    if (locationKey == LocationKey(NetworkType.cycling, Country.nl, "example")) {
      Some(LocationDetailsPageExample.page)
    }
    else {
      buildPage(language, locationKey)
    }
  }

  private def buildPage(language: Language, locationKey: LocationKey): Option[LocationDetailsPage] = {
    val subset = locationService.toSubset(language, locationKey)
    val summary = locationRepository.summary(subset)
    val distance = locationRepository.distance(subset)
    val nameParts = locationKey.name.split(":")
    val locationInfos = nameParts.zipWithIndex.map { case (namePart, index) =>
      val names = nameParts.take(index + 1)
      val link = s"${locationKey.networkType.name}/${locationKey.country.domain}/${names.mkString(":")}"
      LocationInfo(
        namePart,
        link
      )
    }

    Some(
      LocationDetailsPage(
        summary,
        distance,
        locationInfos
      )
    )
  }
}
