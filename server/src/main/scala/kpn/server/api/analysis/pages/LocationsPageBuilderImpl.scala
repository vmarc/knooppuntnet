package kpn.server.api.analysis.pages

import kpn.api.common.Language
import kpn.api.common.location.LocationNode
import kpn.api.common.location.LocationsPage
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.location.LocationConfiguration
import kpn.server.analyzer.engine.analysis.location.LocationDefinition
import kpn.server.repository.LocationRepository
import org.springframework.stereotype.Component

@Component
class LocationsPageBuilderImpl(
  locationConfiguration: LocationConfiguration,
  locationRepository: LocationRepository
) extends LocationsPageBuilder {

  private val log = Log(classOf[LocationsPageBuilderImpl])

  override def build(language: Language, networkType: NetworkType, country: Country): Option[LocationsPage] = {
    val locationNode = locationConfiguration.locations.find(_.name == country.domain) match {
      case Some(locationDefinition) =>
        val nodeCounts = locationRepository.countryLocations(networkType, country).map(l => l.name -> l.count).toMap
        Some(toLocationNode(language, nodeCounts, locationDefinition))
      case None =>
        log.error(s"No locations found for country ${country.domain}")
        None
    }
    Some(
      LocationsPage(locationNode)
    )
  }

  private def toLocationNode(language: Language, nodeCounts: Map[String, Long], locationDefinition: LocationDefinition): LocationNode = {
    val name = locationDefinition.name
    val localName = {
      locationDefinition.locationNames.get(language) match {
        case None => None
        case Some(localLocationName) =>
          if (localLocationName != name) {
            Some(localLocationName)
          }
          else {
            None
          }
      }
    }

    val count = nodeCounts.getOrElse(locationDefinition.name, 0L)
    val children = locationDefinition.children.map(ld => toLocationNode(language, nodeCounts, ld))
    LocationNode(name, localName, count, children)
  }
}
