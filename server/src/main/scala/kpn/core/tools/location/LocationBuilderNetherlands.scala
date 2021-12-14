package kpn.core.tools.location

import kpn.core.doc.LocationPath
import kpn.core.poi.PoiLocation.belgiumAndNetherlands
import kpn.core.util.Log
import org.geotools.geometry.jts.JTS
import org.locationtech.jts.geom.Envelope

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable.ListBuffer
import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable

class LocationBuilderNetherlands(dir: String) {

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
      val locationJsons = InterpretedLocationJson.load(regionsFilename)
      val locationJson = locationJsons.filter(_.name == "Nederland").head
      val name = locationJson.tags("name")
      val names = locationJson.names

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
      val provinceJsons = InterpretedLocationJson.load(provincesFilename)
      provinceJsons.zipWithIndex.foreach { case (provinceJson, index) =>
        val id = s"nl-1-${provinceJson.tags("ref").toLowerCase}"
        val name = provinceJson.tags("name")
        log.info(s"${index + 1}/${provinceJsons.size} $id $name")
        val data = LocationData(
          id,
          LocationDoc(id, Seq(LocationPath(Seq("nl"))), name, provinceJson.names),
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
      val context = Log.contextMessages
      municipalityJsons.par.foreach { municipalityJson =>
        val index = count.incrementAndGet()
        Log.context(context :+ s"$index/${municipalityJsons.size}") {
          val id = s"nl-2-${municipalityJson.tags("ref:gemeentecode").toLowerCase}"
          val name = municipalityJson.tags("name")
          log.info(s"$id $name")
          val names = municipalityJson.names
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

  private def loadMunicipalityJsons(): Seq[InterpretedLocationJson] = {
    InterpretedLocationJson.load(municipalitiesFilename)
      .filter(_.tags.get("boundary").contains("administrative"))
      .filter(_.tags.contains("ref:gemeentecode"))
  }

  private def addLocation(data: LocationData): Unit = {
    synchronized {
      locationDatas += data
    }
  }
}
