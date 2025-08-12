package kpn.core.tools.location

import kpn.api.common.Country
import kpn.core.tools.config.Dirs
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
    Country.values.foreach { country =>
      val dir = s"${Dirs.root}/locations/${country.entryName}/geometries"
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
    val geoJsons = Country.values.flatMap { country =>
      val dir = s"${Dirs.root}/locations/${country.entryName}/geometries"
      new File(dir).listFiles().flatMap { file =>
        val geoJson = FileUtils.readFileToString(file, "UTF-8")
        Option.when(geoJson.contains("EPSG:0")) {
          val geometryType = geoJson.takeWhile(_ != ',').drop("""{"type":"""".length).dropRight(1)
          s"${file.getName} -> $geometryType"
        }
      }
    }
    geoJsons.foreach(println)
    println(s"${geoJsons.size} locations with wrong coordinate reference system")
  }

  def printRootGeometryTypes(): Unit = {
    val geometryTypeMap = Country.values.flatMap { country =>
      val dir = s"${Dirs.root}/locations/${country.entryName}/geometries"
      new File(dir).listFiles().map { file =>
        val geoJson = FileUtils.readFileToString(file, "UTF-8")
        val geometryType = geoJson.takeWhile(_ != ',').drop("""{"type":"""".length).dropRight(1)
        geometryType -> file.getName
      }
    }.groupBy(_._1)

    geometryTypeMap.foreach { case (key, values) => println(s"$key -> ${values.size}") }
  }
}
