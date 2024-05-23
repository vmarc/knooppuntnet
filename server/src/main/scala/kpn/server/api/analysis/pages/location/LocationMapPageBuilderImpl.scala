package kpn.server.api.analysis.pages.location

import kpn.api.common.Language
import kpn.api.common.location.LocationMapPage
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.core.tools.config.Dirs
import kpn.core.util.GeometryUtil
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.analyzer.engine.analysis.location.ParcDuVercors
import kpn.server.repository.LocationRepository
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.geojson.GeoJsonReader
import org.locationtech.jts.io.geojson.GeoJsonWriter
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
    val subset = locationService.toSubset(language, locationKeyParam)
    val summary = locationRepository.summary(subset)

    if (locationKeyParam.name == "Parc du Vercors") {
      val geometries = ParcDuVercors.communes.map { locationId =>
        val filename = s"${Dirs.root}/locations/${locationKeyParam.country.domain}/geometries/$locationId.json"
        var geoJson = FileUtils.readFileToString(new File(filename), "UTF-8")
        geoJson = geoJson.replace("EPSG:0", "EPSG:4326")
        new GeoJsonReader().read(geoJson)
      }
      val geometryCollection = new GeometryFactory().createGeometryCollection(geometries.toArray)
      val geoJsonWriter = new GeoJsonWriter()
      geoJsonWriter.setEncodeCRS(false)
      val geoJson = geoJsonWriter.write(geometryCollection)
      val bounds = GeometryUtil.bounds(geometryCollection)
      Some(
        LocationMapPage(
          summary,
          bounds,
          geoJson
        )
      )
    }
    else {
      val filename = s"${Dirs.root}/locations/${locationKeyParam.country.domain}/geometries/${subset.locationIds.head}.json"
      var geoJson = FileUtils.readFileToString(new File(filename), "UTF-8")
      geoJson = geoJson.replace("EPSG:0", "EPSG:4326")
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
}
