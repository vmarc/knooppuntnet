package kpn.server.api.analysis.pages

import kpn.api.common.Language
import kpn.api.common.location.LocationNode
import kpn.api.common.poi.PoiLocationsPage
import kpn.api.custom.Country
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.location.LocationConfiguration
import kpn.server.analyzer.engine.analysis.location.LocationDefinition
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component

@Component
class PoiLocationsPageBuilder(
  locationConfiguration: LocationConfiguration
) {

  private val log = Log(classOf[PoiLocationsPageBuilder])

  def build(country: Country, language: Language): PoiLocationsPage = {
    val locationNode = locationConfiguration.locations.find(_.id == country.domain) match {
      case Some(locationDefinition) => Some(toLocationNode(language, locationDefinition))
      case None =>
        log.error(s"No locations found for country ${country.domain}")
        None

    }
    PoiLocationsPage(locationNode)
  }

  private def toLocationNode(language: Language, locationDefinition: LocationDefinition): LocationNode = {
    val name = locationDefinition.locationNames.get(language) match {
      case None => locationDefinition.name
      case Some(localLocationName) => localLocationName
    }
    val children = locationDefinition.children.map(ld => toLocationNode(language, ld)).map { locationNode =>
      LocationNodeItem(StringUtils.stripAccents(locationNode.name).toLowerCase, locationNode)
    }.sortWith(byNormalizedName).map(_.locationNode)
    LocationNode(name, None, if (children.isEmpty) None else Some(children))
  }

  // TODO share with LocationsPageBuilderImpl?
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
