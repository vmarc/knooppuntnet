package kpn.core.tools.location

import kpn.core.util.Log

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable

class LocationBuilderBelgium(dir: String) {

  private val countryFilename = s"$dir/be-level-2.geojson.gz"
  private val regionsFilename = s"$dir/be-level-4.geojson.gz"
  private val provincesFilename = s"$dir/be-level-6.geojson.gz"
  private val municipalitiesFilename = s"$dir/be-level-8.geojson.gz"
  private val locationDatas = new LocationDatas()

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
      val locationJson = InterpretedLocationJson.load(countryFilename).head
      locationDatas.add(
        LocationData(
          "be",
          locationJson.name,
          locationJson.names,
          LocationGeometry(locationJson.geometry)
        )
      )
    }
  }

  private def buildBrusselsCapital(): Unit = {
    Log.context("Brussels-Capital") {
      val regions = InterpretedLocationJson.load(regionsFilename)
      val locationJson = regions.find(_.hasTag("ref:INS", "04000")).get
      locationDatas.add(
        LocationData.from(
          "be-1-04000",
          Seq("be"),
          locationJson.name,
          locationJson.names,
          LocationGeometry(locationJson.geometry)
        )
      )
    }
  }

  private def buildProvinces(): Unit = {
    Log.context("provinces") {
      val provinceJsons = InterpretedLocationJson.load(provincesFilename).filter(_.tags.contains("ref:INS"))
      provinceJsons.zipWithIndex.foreach { case (provinceJson, index) =>
        val id = s"be-1-${provinceJson.tags("ref:INS")}"
        log.info(s"${index + 1}/${provinceJsons.size} $id ${provinceJson.name}")
        locationDatas.add(
          LocationData.from(
            id,
            Seq("be"),
            provinceJson.name,
            provinceJson.names,
            LocationGeometry(provinceJson.geometry)
          )
        )
      }
    }
  }

  private def loadMunicipalities(): Unit = {
    Log.context("municipalities") {
      val provinces = locationDatas.startingWith("be-1")
      val municipalityJsons = InterpretedLocationJson.load(municipalitiesFilename)
      val count = new AtomicInteger(0)
      val context = Log.contextMessages
      municipalityJsons.par.foreach { municipalityJson =>
        Log.context(context) {
          val index = count.incrementAndGet()
          Log.context(s"$index/${municipalityJsons.size}") {
            val id = s"be-2-${municipalityJson.tags("ref:INS")}"
            log.info(s"$id ${municipalityJson.name}")
            val municipalityGeometry = LocationGeometry(municipalityJson.geometry)
            provinces.find(_.contains(municipalityGeometry)) match {
              case None => log.error(s"No parent found for municipality $id")
              case Some(province) =>
                locationDatas.add(
                  LocationData.from(
                    id,
                    Seq("be", province.id),
                    municipalityJson.name,
                    municipalityJson.names,
                    LocationGeometry(municipalityJson.geometry)
                  )
                )
            }
          }
        }
      }
    }
  }
}
