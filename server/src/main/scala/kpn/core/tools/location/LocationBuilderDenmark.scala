package kpn.core.tools.location

import kpn.core.util.Log

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable

class LocationBuilderDenmark(dir: String) {

  private val countryFilename = s"$dir/dk-level-2.geojson.gz"
  private val regionsFilename = s"$dir/dk-level-4.geojson.gz"
  private val municipalitiesFilename = s"$dir/dk-level-7.geojson.gz"
  private val locationDatas = new LocationDatas()

  private val log = Log(classOf[LocationBuilderDenmark])

  def build(): Seq[LocationData] = {
    Log.context("dk") {
      buildCountry()
      buildRegions()
      loadMunicipalities()
      locationDatas.toSeq
    }
  }

  private def buildCountry(): Unit = {
    Log.context("country") {
      val locationJson = InterpretedLocationJson.load(countryFilename).head
      locationDatas.add(
        LocationData(
          "dk",
          locationJson.name,
          locationJson.names,
          LocationGeometry(locationJson.geometry)
        )
      )
    }
  }

  private def buildRegions(): Unit = {
    Log.context("regions") {
      val regions = InterpretedLocationJson.load(regionsFilename)
      regions.zipWithIndex.foreach { case (region, index) =>
        val id = s"dk-1-${region.tags("ref:nuts")}"
        log.info(s"${index + 1}/${regions.size} $id ${region.name}")
        locationDatas.add(
          LocationData.from(
            id,
            Seq("dk"),
            region.name,
            region.names,
            LocationGeometry(region.geometry)
          )
        )
      }
    }
  }

  private def loadMunicipalities(): Unit = {
    Log.context("municipalities") {
      val regions = locationDatas.startingWith("dk-1")
      val municipalities = InterpretedLocationJson.load(municipalitiesFilename)
      val count = new AtomicInteger(0)
      val context = Log.contextMessages
      municipalities.par.foreach { municipality =>
        Log.context(context) {
          val index = count.incrementAndGet()
          Log.context(s"$index/${municipalities.size}") {
            val id = s"dk-2-${municipality.tags("ref")}"
            log.info(s"$id ${municipality.name}")
            val municipalityGeometry = LocationGeometry(municipality.geometry)
            regions.find(_.contains(municipalityGeometry)) match {
              case None => throw new RuntimeException(s"No parent found for municipality $id")
              case Some(region) =>
                locationDatas.add(
                  LocationData.from(
                    id,
                    Seq("dk", region.id),
                    municipality.name,
                    municipality.names,
                    LocationGeometry(municipality.geometry)
                  )
                )
            }
          }
        }
      }
    }
  }
}
