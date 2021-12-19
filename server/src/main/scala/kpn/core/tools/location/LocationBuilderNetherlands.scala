package kpn.core.tools.location

import kpn.core.poi.PoiLocation.belgiumAndNetherlands
import kpn.core.util.Log
import org.geotools.geometry.jts.JTS
import org.locationtech.jts.geom.Envelope

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable

class LocationBuilderNetherlands(dir: String) {

  private val regionsFilename = s"$dir/nl-level-3.geojson.gz"
  private val provincesFilename = s"$dir/nl-level-4.geojson.gz"
  private val municipalitiesFilename = s"$dir/nl-level-8.geojson.gz"
  private val locationDatas = new LocationDatas()

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
      val regions = InterpretedLocationJson.load(regionsFilename)
      val country = regions.filter(_.name == "Nederland").head
      val name = country.tags("name")
      val names = country.names

      val bb = belgiumAndNetherlands
      val envelope = new Envelope(bb.xMin, bb.xMax, bb.yMin, bb.yMax)
      val geometry = country.geometry.intersection(JTS.toGeometry(envelope))

      locationDatas.add(
        LocationData(
          "nl",
          name,
          names,
          LocationGeometry(geometry)
        )
      )
    }
  }

  private def buildProvinces(): Unit = {
    Log.context("provinces") {
      val provinces = InterpretedLocationJson.load(provincesFilename)
      provinces.zipWithIndex.foreach { case (province, index) =>
        val id = s"nl-1-${province.tags("ref").toLowerCase}"
        val name = province.tags("name")
        log.info(s"${index + 1}/${provinces.size} $id $name")
        locationDatas.add(
          LocationData.from(
            id,
            Seq("nl"),
            name,
            province.names,
            LocationGeometry(province.geometry)
          )
        )
      }
    }
  }

  private def loadMunicipalities(): Unit = {
    Log.context("municipalities") {
      val provinces = locationDatas.startingWith("nl-1")
      val municipalities = InterpretedLocationJson.load(municipalitiesFilename).filter(_.tags.contains("ref:gemeentecode"))
      val count = new AtomicInteger(0)
      val context = Log.contextMessages
      municipalities.par.foreach { municipality =>
        val index = count.incrementAndGet()
        Log.context(context :+ s"$index/${municipalities.size}") {
          val id = s"nl-2-${municipality.tags("ref:gemeentecode").toLowerCase}"
          val name = municipality.tags("name")
          log.info(s"$id $name")
          val names = municipality.names
          val geometry = LocationGeometry(municipality.geometry)
          provinces.find(_.contains(geometry)) match {
            case None => throw new RuntimeException(s"Province not found for municipality $id $name")
            case Some(province) =>
              val parents = Seq("nl", province.id)
              locationDatas.add(
                LocationData.from(
                  id,
                  parents,
                  name,
                  names,
                  LocationGeometry(municipality.geometry)
                )
              )
          }
        }
      }
    }
  }
}
