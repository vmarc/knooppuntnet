package kpn.server.analyzer.engine.analysis.location

import java.io.File

import kpn.core.database.query.Fields
import kpn.core.db.couch.Couch
import kpn.shared.Language
import kpn.shared.Languages
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.Envelope
import org.locationtech.jts.geom.Geometry

object LocationDefinitionReader {

  private case class LocationJsonProperties(name: String, alltags: Map[String, String])

  private case class LocationJson(properties: LocationJsonProperties, bbox: Seq[String], geometry: Geometry)

}

class LocationDefinitionReader(file: File) {

  import LocationDefinitionReader._

  def read(children: Seq[LocationDefinition] = Seq.empty): LocationDefinition = {
    val json = FileUtils.readFileToString(file)

    val locationJson = Couch.objectMapper.readValue(json, classOf[LocationJson])

    val locationNames = parseLocationNames(locationJson)
    val boundingBox = parseBoundingBox(locationJson)

    LocationDefinition(locationJson.properties.name, locationNames, filename, boundingBox, locationJson.geometry, children)
  }

  private def parseLocationNames(locationJson: LocationJson): Map[Language, String] = {
    Languages.all.flatMap { language =>
      val key = "name:" + language.toString.toLowerCase
      locationJson.properties.alltags.get(key).map(value => language -> value.toString)
    }.toMap
  }

  private def parseBoundingBox(locationJson: LocationJson): Envelope = {
    if (locationJson.bbox.size != 4) {
      throw error("Field 'bbox' does not contain 4 elements")
    }

    val bbox = Fields(locationJson.bbox)
    val latMin = bbox.double(0)
    val latMax = bbox.double(1)
    val lonMin = bbox.double(2)
    val lonMax = bbox.double(3)
    new Envelope(latMin, lonMin, latMax, lonMax)
  }


  private def error(message: String): RuntimeException = {
    new RuntimeException(s"Error parsing file '${file.getAbsolutePath}': $message")
  }

  private def filename: String = {
    val path = file.getAbsolutePath
    val a = LocationConfigurationDefinition.DIR.length
    val b = LocationConfigurationDefinition.EXTENSION.length
    path.drop(a).dropRight(b)
  }

}
