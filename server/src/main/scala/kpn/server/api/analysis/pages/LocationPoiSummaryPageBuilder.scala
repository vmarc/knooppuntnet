package kpn.server.api.analysis.pages

import kpn.api.common.Language
import kpn.api.common.poi.LocationPoiSummaryPage
import kpn.api.common.poi.PoiCount
import kpn.api.common.poi.PoiGroup
import kpn.api.custom.LocationKey
import kpn.core.poi.PoiConfiguration
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.repository.PoiRepository
import org.springframework.stereotype.Component

@Component
class LocationPoiSummaryPageBuilder(
  poiRepository: PoiRepository,
  locationService: LocationService
) {

  def build(
    language: Language,
    locationKeyParam: LocationKey
  ): LocationPoiSummaryPage = {
    val locationKey = locationService.translate(language, locationKeyParam)
    val counts = poiRepository.locationPoiLayerCounts(locationKey.name)
    val poiCountMap = counts.map(count => count.layer -> count.count).toMap
    val groups = PoiConfiguration.instance.groupDefinitions.map { groupDefinition =>
      val poiCounts = groupDefinition.definitions.map { poiDefinition =>
        val count = poiCountMap.getOrElse(poiDefinition.name, 0L)
        PoiCount(poiDefinition.name, poiDefinition.icon, count)
      }
      PoiGroup(groupDefinition.name, poiCounts)
    }
    LocationPoiSummaryPage(
      groups
    )
  }
}
