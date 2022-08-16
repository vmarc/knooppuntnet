package kpn.core.tools.location

import kpn.api.custom.Country
import org.apache.commons.io.FileUtils
import org.locationtech.jts.io.geojson.GeoJsonReader

import java.io.File

object LocationGeometryExplorationTool {
  def main(args: Array[String]): Unit = {
    new LocationGeometryExplorationTool().printLocationsWithWrongCoordinateReferenceSystem()
  }

}

class LocationGeometryExplorationTool {
  def printLocationsWithGeometryCollectionWithMultipleElements(): Unit = {
    Country.all.foreach { country =>
      val dir = s"/kpn/locations/${country.domain}/geometries"
      new File(dir).listFiles().foreach { file =>
        val geoJson = FileUtils.readFileToString(file, "UTF-8")
        val geometry = new GeoJsonReader().read(geoJson)
        if (geometry.getGeometryType == "GeometryCollection") {
          if (geometry.getNumGeometries > 1) {
            println(s"${file.getName} -> ${geometry.getNumGeometries}")
          }
        }
      }
    }
  }

  def printLocationsWithWrongCoordinateReferenceSystem(): Unit = {
    val geojsons = Country.all.flatMap { country =>
      val dir = s"/kpn/locations/${country.domain}/geometries"
      new File(dir).listFiles().flatMap { file =>
        val geoJson = FileUtils.readFileToString(file, "UTF-8")
        if (geoJson.contains("EPSG:0")) {
          val geometryType = geoJson.takeWhile(_ != ',').drop("""{"type":"""".length).dropRight(1)
          Some(s"${file.getName} -> $geometryType")
        }
        else {
          None
        }
      }
    }
    geojsons.foreach(println)
    println(s"${geojsons.size} locations with wrong coordinate reference system")

  }

  def printRootGeometryTypes(): Unit = {
    val geometryTypeMap = Country.all.flatMap { country =>
      val dir = s"/kpn/locations/${country.domain}/geometries"
      new File(dir).listFiles().map { file =>
        val geoJson = FileUtils.readFileToString(file, "UTF-8")
        val geometryType = geoJson.takeWhile(_ != ',').drop("""{"type":"""".length).dropRight(1)
        geometryType -> file.getName
      }
    }.groupBy(_._1)

    geometryTypeMap.foreach { case (key, values) => println(s"$key -> ${values.size}") }
  }
}
