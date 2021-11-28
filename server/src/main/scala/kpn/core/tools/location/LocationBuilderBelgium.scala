package kpn.core.tools.location

import kpn.core.doc.LocationPath
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.location.LocationDefinitionReader.LocationsJson
import kpn.server.json.Json

import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.concurrent.atomic.AtomicInteger
import java.util.zip.GZIPInputStream
import scala.collection.mutable.ListBuffer
import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable

class LocationBuilderBelgium {

  private val dir = "/kpn/locations"
  private val countryFilename = s"$dir/be-level-2.geojson.gz"
  private val regionsFilename = s"$dir/nl-level-4.geojson.gz"
  private val provincesFilename = s"$dir/nl-level-6.geojson.gz"
  private val municipalitiesFilename = s"$dir/nl-level-8.geojson.gz"
  private val locationDatas = ListBuffer[LocationData]()

  private val log = Log(classOf[LocationBuilderBelgium])

  def build(): Seq[LocationData] = {
    Log.context("be") {
      buildCountry()
      buildBrusselsCapital()
      buildProvinces()
      loadMunicipalities()
      locationDatas.toSeq
    }
  }

  private def buildCountry(): Unit = {
    Log.context("country") {
      val locationJson = loadLocationJsons(countryFilename).head
      val data = LocationData(
        "be",
        LocationDoc("be", Seq.empty, locationJson.name, locationJson.names),
        LocationGeometry(locationJson.geometry)
      )
      addLocation(data)
    }
  }

  private def buildBrusselsCapital(): Unit = {
    Log.context("Brussels-Capital") {
      val locationJson = loadLocationJsons(regionsFilename).find(_.tags.get("ref:INS").contains("04000")).get
      val data = LocationData(
        "be-1-04000",
        LocationDoc("be-1-04000", Seq(LocationPath(Seq("be"))), locationJson.name, locationJson.names),
        LocationGeometry(locationJson.geometry)
      )
      addLocation(data)
    }
  }

  private def buildProvinces(): Unit = {
    Log.context("provinces") {
      val provinceJsons = loadLocationJsons(provincesFilename).filter(_.tags.contains("ref:INS"))
      val count = new AtomicInteger(0)
      provinceJsons.foreach { provinceJson =>
        val index = count.incrementAndGet()
        val id = s"be-1-${provinceJson.tags("ref:INS")}"
        log.info(s"$index/${provinceJsons.size} $id ${provinceJson.name}")
        val data = LocationData(
          id,
          LocationDoc(id, Seq(LocationPath(Seq("be"))), provinceJson.name, provinceJson.names),
          LocationGeometry(provinceJson.geometry)
        )
        addLocation(data)
      }
    }
  }

  private def loadMunicipalities(): Unit = {
    Log.context("municipalities") {
      val provinces = locationDatas.toSeq.filter(_.id.startsWith("be-1"))
      val municipalityJsons = loadLocationJsons(municipalitiesFilename)
      val count = new AtomicInteger(0)
      municipalityJsons.par.foreach { municipalityJson =>
        val index = count.incrementAndGet()
        Log.context(s"$index/${municipalityJsons.size}") {
          val id = s"be-2-${municipalityJson.tags("ref:INS")}"
          log.info(s"$id ${municipalityJson.name}")
          val geometry = LocationGeometry(municipalityJson.geometry)
          provinces.find(province => province.geometry.contains(geometry)) match {
            case None => log.error(s"No parent found for municipality $id")
            case Some(province) =>
              val parents = Seq("be", province.id)
              val data = LocationData(
                id,
                LocationDoc(id, Seq(LocationPath(parents)), municipalityJson.name, municipalityJson.names),
                LocationGeometry(municipalityJson.geometry)
              )
              addLocation(data)
          }
        }
      }
    }
  }

  private def loadLocationJsons(filename: String): Seq[InterpretedLocationJson] = {
    val gzippedInputStream = new FileInputStream(filename)
    val ungzippedInputStream = new GZIPInputStream(gzippedInputStream)
    val fileReader = new InputStreamReader(ungzippedInputStream, "UTF-8")
    Json.objectMapper.readValue(fileReader, classOf[LocationsJson]).features.map(InterpretedLocationJson)
  }

  private def addLocation(data: LocationData): Unit = {
    synchronized {
      locationDatas += data
    }
  }
}
