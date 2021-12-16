package kpn.core.tools.location

import kpn.core.doc.LocationPath
import kpn.core.util.Log
import org.geotools.geometry.jts.GeometryClipper
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LinearRing
import org.locationtech.jts.geom.impl.CoordinateArraySequence

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
      val locationJson = InterpretedLocationJson.load(countryFilename).head
      val geometry = clipSpain(locationJson.geometry)
      val data = LocationData(
        "es",
        LocationDoc("es", Seq.empty, locationJson.name, locationJson.names),
        LocationGeometry(geometry)
      )
      locationDatas.add(data)
    }
  }

  private def buildProvincies(): Unit = {
    Log.context("provinces") {
      val provinces = InterpretedLocationJson.load(provincesFilename)
      provinces.zipWithIndex.foreach { case (province, index) =>
        val id = s"es-1-${province.tags("ine:provincia")}"
        log.info(s"${index + 1}/${provinces.size} $id ${province.name}")
        val data = LocationData(
          id,
          LocationDoc(id, Seq(LocationPath(Seq("es"))), province.name, province.names),
          LocationGeometry(province.geometry)
        )
        locationDatas.add(data)
      }
    }
  }

  private def clipSpain(geometry: Geometry): Geometry = {
    val coordinates = Seq(
      new Coordinate(-9.92, 44.08),
      new Coordinate(4.81, 44.08),
      new Coordinate(4.81, 35.77),
      new Coordinate(-9.92, 35.77),
      new Coordinate(-9.92, 44.08),
    )
    val factory = new GeometryFactory()
    val boundingBox = new LinearRing(new CoordinateArraySequence(coordinates.toArray), factory)
    val clipper = new GeometryClipper(boundingBox.getEnvelopeInternal)
    clipper.clipSafe(geometry, false, 0.00001)
  }
}
