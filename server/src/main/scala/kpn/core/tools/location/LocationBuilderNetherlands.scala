package kpn.core.tools.location

import kpn.api.common.Languages
import kpn.core.doc.LocationName
import kpn.core.doc.LocationPath
import kpn.core.poi.PoiLocation.belgiumAndNetherlands
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.location.LocationDefinitionReader.LocationJson
import kpn.server.analyzer.engine.analysis.location.LocationDefinitionReader.LocationsJson
import kpn.server.json.Json
import org.geotools.geometry.jts.JTS
import org.locationtech.jts.geom.Envelope

import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.concurrent.atomic.AtomicInteger
import java.util.zip.GZIPInputStream
import scala.collection.mutable.ListBuffer
import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable

class LocationBuilderNetherlands {

  private val dir = "/kpn/locations"
  private val regionsFilename = s"$dir/nl-level-3.geojson.gz"
  private val provincesFilename = s"$dir/nl-level-4.geojson.gz"
  private val municipalitiesFilename = s"$dir/nl-level-8.geojson.gz"
  private val locationDatas = ListBuffer[LocationData]()

  private val log = Log(classOf[LocationBuilderNetherlands])

  def build(): Seq[LocationData] = {
    Log.context("nl") {
      buildCountry()
      buildProvinces()
      loadMunicipalities()
      locationDatas.toSeq
    }
  }

  private def buildCountry(): Unit = {
    Log.context("country") {
      val locationJsons = loadLocationJsons(regionsFilename)
      val locationJson = locationJsons.filter(_.properties.name == "Netherlands").head
      val name = locationJson.properties.all_tags("name")
      val names = locationNames(locationJson)

      val bb = belgiumAndNetherlands
      val envelope = new Envelope(bb.xMin, bb.xMax, bb.yMin, bb.yMax)
      val geometry = locationJson.geometry.intersection(JTS.toGeometry(envelope))

      val data = LocationData(
        "nl",
        LocationDoc("nl", Seq.empty, name, names),
        LocationGeometry(geometry)
      )
      addLocation(data)
    }
  }

  private def buildProvinces(): Unit = {
    Log.context("provinces") {
      val provinceJsons = loadLocationJsons(provincesFilename)
      val count = new AtomicInteger(0)
      provinceJsons.foreach { provinceJson =>
        val index = count.incrementAndGet()
        val id = s"nl-1-${provinceJson.properties.all_tags("ref").toLowerCase}"
        val name = provinceJson.properties.all_tags("name")
        log.info(s"$index/${provinceJsons.size} $id $name")
        val names = locationNames(provinceJson)
        val data = LocationData(
          id,
          LocationDoc(id, Seq(LocationPath(Seq("nl"))), name, names),
          LocationGeometry(provinceJson.geometry)
        )
        addLocation(data)
      }
    }
  }

  private def loadMunicipalities(): Unit = {
    Log.context("municipalities") {
      val provinces = locationDatas.toSeq.filter(_.id.startsWith("nl-1"))
      val municipalityJsons = loadMunicipalityJsons()
      val count = new AtomicInteger(0)
      municipalityJsons.par.foreach { municipalityJson =>
        val index = count.incrementAndGet()
        Log.context(s"$index/${municipalityJsons.size}") {
          val id = s"nl-2-${municipalityJson.properties.all_tags("ref:gemeentecode").toLowerCase}"
          val name = municipalityJson.properties.all_tags("name")
          log.info(s"$id $name")
          val names = locationNames(municipalityJson)
          val geometry = LocationGeometry(municipalityJson.geometry)
          provinces.find(province => province.geometry.contains(geometry)) match {
            case None => log.error("No parent found for municipality $id")
            case Some(province) =>
              val parents = Seq("nl", province.id)
              val data = LocationData(
                id,
                LocationDoc(id, Seq(LocationPath(parents)), name, names),
                LocationGeometry(municipalityJson.geometry)
              )
              addLocation(data)
          }
        }
      }
    }
  }

  private def loadMunicipalityJsons(): Seq[LocationJson] = {
    loadLocationJsons(municipalitiesFilename)
      .filter(_.properties.all_tags.get("boundary").contains("administrative"))
      .filter(_.properties.all_tags.contains("ref:gemeentecode"))
  }

  private def loadLocationJsons(filename: String): Seq[LocationJson] = {
    val gzippedInputStream = new FileInputStream(filename)
    val ungzippedInputStream = new GZIPInputStream(gzippedInputStream)
    val fileReader = new InputStreamReader(ungzippedInputStream, "UTF-8")
    Json.objectMapper.readValue(fileReader, classOf[LocationsJson]).features
  }

  private def locationNames(locationJson: LocationJson): Seq[LocationName] = {
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

  private def addLocation(data: LocationData): Unit = {
    synchronized {
      locationDatas += data
    }
  }
}
