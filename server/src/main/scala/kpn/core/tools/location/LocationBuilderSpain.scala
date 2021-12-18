package kpn.core.tools.location

import kpn.core.util.GeometryUtil
import kpn.core.util.Log
import org.locationtech.jts.geom.Geometry

class LocationBuilderSpain(dir: String) {

  private val countryFilename = s"$dir/es-level-2.geojson.gz"
  private val provincesFilename = s"$dir/es-level-6.geojson.gz"
  private val locationDatas = new LocationDatas()

  private val log = Log(classOf[LocationBuilderSpain])

  def build(): Seq[LocationData] = {
    Log.context("es") {
      buildCountry()
      buildProvincies()
      locationDatas.toSeq
    }
  }

  private def buildCountry(): Unit = {
    Log.context("country") {
      val country = InterpretedLocationJson.load(countryFilename).head
      val geometry = clipSpain(country.geometry)
      locationDatas.add(
        LocationData(
          "es",
          country.name,
          country.names,
          LocationGeometry(geometry)
        )
      )
    }
  }

  private def buildProvincies(): Unit = {
    Log.context("provinces") {
      val provinces = InterpretedLocationJson.load(provincesFilename)
      provinces.zipWithIndex.foreach { case (province, index) =>
        val id = s"es-1-${province.tags("ine:provincia")}"
        log.info(s"${index + 1}/${provinces.size} $id ${province.name}")
        locationDatas.add(
          LocationData.from(
            id,
            Seq("es"),
            province.name,
            province.names,
            LocationGeometry(province.geometry)
          )
        )
      }
    }
  }

  private def clipSpain(geometry: Geometry): Geometry = {
    val envelope = GeometryUtil.envelope(-9.92, 44.08, 4.81, 35.77)
    GeometryUtil.clip(geometry, envelope)
  }
}
