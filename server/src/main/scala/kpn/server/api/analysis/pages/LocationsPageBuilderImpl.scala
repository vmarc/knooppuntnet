package kpn.server.api.analysis.pages

import kpn.api.common.Language
import kpn.api.common.location.LocationNode
import kpn.api.common.location.LocationsPage
import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.actions.locations.LocationQueryResult
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

  override def build(language: Language, subset: Subset): Option[LocationsPage] = {
    val locationNode = locationConfiguration.locations.find(_.id == subset.country.domain) match {
      case Some(locationDefinition) =>
        val nodeCounts = log.infoElapsed {
          val result = locationRepository.countryLocations(subset).map(l => l.name -> l).toMap
          (s"location counts size=${result.size}", result)
        }
        log.infoElapsed {
          val result = Some(toLocationNode(language, nodeCounts, locationDefinition))
          ("toLocationNode", result)
        }

      case None =>
        log.error(s"No locations found for subset ${subset.name}")
        None
    }
    Some(
      LocationsPage(locationNode)
    )
  }

  private def toLocationNode(language: Language, nodeCounts: Map[String, LocationQueryResult], locationDefinition: LocationDefinition): LocationNode = {
    val name = locationDefinition.locationNames.get(language) match {
      case None => locationDefinition.name
      case Some(localLocationName) => localLocationName
    }
    val count = nodeCounts.getOrElse(locationDefinition.id, LocationQueryResult(locationDefinition.id, 0, 0, 0))
    val children = locationDefinition.children.map(ld => toLocationNode(language, nodeCounts, ld)).map { locationNode =>
      LocationNodeItem(StringUtils.stripAccents(locationNode.name).toLowerCase, locationNode)
    }.sortWith(byNormalizedName).map(_.locationNode)
    LocationNode(
      name,
      count.nodeCount,
      count.routeCount,
      count.factCount,
      if (children.isEmpty) None else Some(children)
    )
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
