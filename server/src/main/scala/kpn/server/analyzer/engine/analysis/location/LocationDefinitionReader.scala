package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.Language
import kpn.api.common.Languages
import kpn.api.custom.Country
import kpn.server.json.Json
import org.locationtech.jts.geom.Geometry

import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.zip.GZIPInputStream

object LocationDefinitionReader {

  private case class LocationJsonProperties(id: String, name: String, all_tags: Map[String, String])

  private case class LocationJson(properties: LocationJsonProperties, bbox: Seq[String], geometry: Geometry)

  private case class LocationsJson(features: Seq[LocationJson])
}

class LocationDefinitionReader(root: String, country: Country) {

  import LocationDefinitionReader._

  def read(): Seq[LocationDefinition] = {

    val locationsJson = readLocationsJson()

    locationsJson.features.map { locationJson =>
      val name = locationJson.properties.name
      val level = locationJson.properties.all_tags("admin_level").toInt
      val id = s"${country.domain}/$level/$name"
      val boundingBox = locationJson.geometry.getEnvelopeInternal
      val locationNames = parseLocationNames(locationJson)

      LocationDefinition(
        id,
        name,
        level,
        locationNames,
        boundingBox,
        locationJson.geometry
      )
    }
  }

  private def readLocationsJson(): LocationsJson = {
    val gzippedInputStream = new FileInputStream(countryFileName)
    val ungzippedInputStream = new GZIPInputStream(gzippedInputStream)
    val fileReader = new InputStreamReader(ungzippedInputStream, "UTF-8")
    Json.objectMapper.readValue(fileReader, classOf[LocationsJson])
  }

  private def parseLocationNames(locationJson: LocationJson): Map[Language, String] = {
    val name = locationJson.properties.name
    Languages.all.flatMap { language =>
      val key = "name:" + language.toString.toLowerCase
      locationJson.properties.all_tags.get(key).filter(_ != name).map(value => language -> value)
    }.toMap
  }

  private def countryFileName: String = {
    s"$root/${country.domain}.geojson.gz"
  }
}
