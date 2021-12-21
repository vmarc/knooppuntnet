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
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component

case class LocationNodeItem(normalizeName: String, locationNode: LocationNode)

@Component
class LocationsPageBuilderImpl(
  locationConfiguration: LocationConfiguration,
  locationRepository: LocationRepository
) extends LocationsPageBuilder {

  private val log = Log(classOf[LocationsPageBuilderImpl])

  override def build(language: Language, networkType: NetworkType, country: Country): Option[LocationsPage] = {
    val locationNode = locationConfiguration.locations.find(_.id == country.domain) match {
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
    val name = locationDefinition.locationNames.get(language) match {
      case None => locationDefinition.name
      case Some(localLocationName) => localLocationName
    }
    val count = nodeCounts.getOrElse(locationDefinition.id, 0L)
    val children = locationDefinition.children.map(ld => toLocationNode(language, nodeCounts, ld)).map { locationNode =>
      LocationNodeItem(StringUtils.stripAccents(locationNode.name).toLowerCase, locationNode)
    }.sortWith(byNormalizedName).map(_.locationNode)
    LocationNode(name, count, children)
  }

  private def byNormalizedName(a: LocationNodeItem, b: LocationNodeItem): Boolean = {
    if (a.locationNode.name.startsWith("Communauté de communes")) {
      if (b.locationNode.name.startsWith("Communauté de communes")) {
        a.normalizeName.compareTo(b.normalizeName) <= 0
      }
      else {
        true
      }
    }
    else if (b.locationNode.name.startsWith("Communauté de communes")) {
      false
    }
    else {
      a.normalizeName.compareTo(b.normalizeName) <= 0
    }
  }
}
