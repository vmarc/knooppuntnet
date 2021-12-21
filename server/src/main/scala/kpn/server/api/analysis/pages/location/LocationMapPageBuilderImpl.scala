package kpn.server.api.analysis.pages.location

import kpn.api.common.Language
import kpn.api.common.location.LocationMapPage
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.core.util.GeometryUtil
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.repository.LocationRepository
import org.apache.commons.io.FileUtils
import org.locationtech.jts.io.geojson.GeoJsonReader
import org.springframework.stereotype.Component

import java.io.File

@Component
class LocationMapPageBuilderImpl(
  locationRepository: LocationRepository,
  locationService: LocationService
) extends LocationMapPageBuilder {

  override def build(language: Language, locationKey: LocationKey): Option[LocationMapPage] = {
    if (locationKey == LocationKey(NetworkType.cycling, Country.nl, "example")) {
      Some(LocationMapPageExample.page)
    }
    else {
      buildPage(language, locationKey)
    }
  }

  private def buildPage(language: Language, locationKeyParam: LocationKey): Option[LocationMapPage] = {
    val locationKey = locationService.translate(language, locationKeyParam)
    val summary = locationRepository.summary(locationKey)
    // TODO try first: ${locationKey.name}-minimized.geojson"
    val filename = s"/kpn/locations/${locationKey.country.domain}/geometries/${locationKey.name}.json"
    val geoJson = FileUtils.readFileToString(new File(filename), "UTF-8")
    val geometry = new GeoJsonReader().read(geoJson)
    val bounds = GeometryUtil.bounds(geometry)
    Some(
      LocationMapPage(
        summary,
        bounds,
        geoJson
      )
    )
  }
}
