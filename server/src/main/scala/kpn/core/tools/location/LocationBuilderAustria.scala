package kpn.core.tools.location

import kpn.api.custom.Tags
import kpn.core.util.Log

class LocationBuilderAustria(dir: String) {

  private val countryFilename = s"$dir/at-level-2.geojson.gz"
  private val federalStatesFilename = s"$dir/at-level-4.geojson.gz"
  private val districtsFilename = s"$dir/at-level-6.geojson.gz"
  private val locationDatas = new LocationDatas()

  private val log = Log(classOf[LocationBuilderAustria])

  def build(): Seq[LocationData] = {
    Log.context("at") {
      buildCountry()
      buildFederalStates()
      buildDistricts()
      locationDatas.toSeq
    }
  }

  private def buildCountry(): Unit = {
    Log.context("country") {
      val locationJson = InterpretedLocationJson.load(countryFilename).head
      locationDatas.add(
        LocationData(
          "at",
          locationJson.relationId,
          locationJson.name,
          locationJson.names,
          Tags.from(locationJson.tags),
          LocationGeometry(locationJson.geometry)
        )
      )
    }
  }

  private def buildFederalStates(): Unit = {
    Log.context("federal states") {
      val federalStates = InterpretedLocationJson.load(federalStatesFilename)
      federalStates.zipWithIndex.foreach { case (federalState, index) =>
        val id = s"at-1-${federalState.tags("ref:at:gkz")}"
        log.info(s"${index + 1}/${federalStates.size} $id ${federalState.name}")
        locationDatas.add(
          LocationData.from(
            id,
            federalState.relationId,
            Seq("at"),
            federalState.name,
            federalState.names,
            Tags.from(federalState.tags),
            LocationGeometry(federalState.geometry)
          )
        )
      }
    }
  }

  private def buildDistricts(): Unit = {
    Log.context("districts") {
      val federalStates = locationDatas.startingWith("at-1")
      val districts = InterpretedLocationJson.load(districtsFilename)
      districts.zipWithIndex.foreach { case (district, index) =>
        Log.context(s"${index + 1}/${districts.size}") {
          val id = s"at-2-${district.tags("ref:at:gkz")}"
          log.info(s"$id ${district.name}")
          val districtGeometry = LocationGeometry(district.geometry)
          federalStates.find(_.contains(districtGeometry)) match {
            case None => throw new RuntimeException(s"Federal state not found for district $id ${district.name}")
            case Some(federalState) =>
              locationDatas.add(
                LocationData.from(
                  id,
                  federalState.relationId,
                  Seq("at", federalState.id),
                  district.name,
                  district.names,
                  federalState.tags,
                  LocationGeometry(district.geometry)
                )
              )
          }
        }
      }
    }
  }
}
