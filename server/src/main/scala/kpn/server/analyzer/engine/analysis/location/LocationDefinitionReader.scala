package kpn.server.analyzer.engine.analysis.location

import java.io.File

import kpn.shared.DE
import kpn.shared.EN
import kpn.shared.FR
import kpn.shared.Language
import kpn.shared.NL
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.Envelope
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.io.geojson.GeoJsonReader
import spray.json._

class LocationDefinitionReader(file: File) {

  def read(children: Seq[LocationDefinition] = Seq.empty): LocationDefinition = {
    val json = FileUtils.readFileToString(file)
    json.parseJson match {
      case rootObject: JsObject =>
        val name = parseName(rootObject)
        val boundingBox = parseBoundingBox(rootObject)
        val geometry = parseGeometry(rootObject)
        val locationNames = parseLocationNames(name, rootObject)
        LocationDefinition(name, locationNames, filename, boundingBox, geometry, children)
      case _ => throw error("Could not find root object")
    }
  }

  private def parseBoundingBox(rootObject: JsObject): Envelope = {
    rootObject.fields("bbox") match {
      case ja: JsArray =>
        if (ja.elements.size != 4) {
          throw error("Field 'bbox' does not contain 4 elements")
        }
        val latMin = ja.elements(0).toString.toDouble
        val latMax = ja.elements(1).toString.toDouble
        val lonMin = ja.elements(2).toString.toDouble
        val lonMax = ja.elements(3).toString.toDouble
        new Envelope(latMin, lonMin, latMax, lonMax)

      case _ => throw error("Could not find field 'bbox' in root object")
    }
  }

  private def parseName(rootObject: JsObject): String = {
    rootObject.fields("properties") match {
      case properties: JsObject =>
        properties.fields("name") match {
          case string: JsString => string.value
          case _ => throw error("Could not find field 'name' in 'properties'")
        }
      case _ => throw error("Could not find field 'properties' in root object")
    }
  }

  private def parseGeometry(rootObject: JsObject): Geometry = {
    rootObject.fields("geometry") match {
      case value: JsValue => new GeoJsonReader().read(value.toString())
      case _ => throw error("Could not find field 'geometry' in root object")
    }
  }

  private def parseLocationNames(name: String, rootObject: JsObject): Map[Language, String] = {
    rootObject.fields("properties") match {
      case properties: JsObject =>
        properties.fields("alltags") match {
          case tags: JsObject =>
            Seq(
              parseLanguageName(EN, name, tags),
              parseLanguageName(NL, name, tags),
              parseLanguageName(DE, name, tags),
              parseLanguageName(FR, name, tags)
            ).flatten.toMap

          case _ => Map.empty
        }
      case _ => Map.empty
    }
  }

  private def parseLanguageName(language: Language, name: String, tags: JsObject): Option[(Language, String)] = {
    val key = "name:" + language.toString.toLowerCase
    tags.fields.get(key) match {
      case Some(value: JsString) =>
        if (value.value != name) {
          Some(language -> value.value)
        }
        else {
          None
        }
      case _ => None
    }
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
