package kpn.core.tools.location

import kpn.api.common.Languages
import kpn.core.doc.LocationName
import kpn.server.analyzer.engine.analysis.location.LocationDefinitionReader.LocationJson
import kpn.server.analyzer.engine.analysis.location.LocationDefinitionReader.LocationsJson
import kpn.server.json.Json
import org.locationtech.jts.geom.Geometry

import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.zip.GZIPInputStream

object InterpretedLocationJson {

  def load(filename: String): Seq[InterpretedLocationJson] = {
    val gzippedInputStream = new FileInputStream(filename)
    val ungzippedInputStream = new GZIPInputStream(gzippedInputStream)
    val fileReader = new InputStreamReader(ungzippedInputStream, "UTF-8")
    val locationJsons = Json.objectMapper.readValue(fileReader, classOf[LocationsJson]).features
    locationJsons.map(locationJson => InterpretedLocationJson(locationJson))
  }
}

case class InterpretedLocationJson(locationJson: LocationJson) {

  def name: String = {
    locationJson.properties.all_tags("name")
  }

  def names: Seq[LocationName] = {
    val name = locationJson.properties.all_tags("name")
    Languages.all.flatMap { language =>
      val lang = language.toString.toLowerCase
      locationJson.properties.all_tags.get(s"name:$lang") match {
        case None => None
        case Some(value) =>
          if (value != name) {
            Some(
              LocationName(
                language,
                value
              )
            )
          }
          else {
            None
          }
      }
    }
  }

  def geometry: Geometry = {
    locationJson.geometry
  }

  def tags: Map[String, String] = {
    locationJson.properties.all_tags
  }
}
