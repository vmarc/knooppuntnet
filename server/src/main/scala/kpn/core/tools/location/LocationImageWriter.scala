package kpn.core.tools.location

import kpn.server.analyzer.engine.analysis.location.LocationTree
import org.locationtech.jts.geom.Geometry

import java.awt.Color

class LocationImageWriter(locationDatas: Seq[LocationData]) {

  def printCountry(country: LocationTree, filename: String): Unit = {

    val locationsLevel1 = country.childLocations
    val countryGeometry = readGeometry(country)
    val imageWriter = new GeometryImageWriter(1000, 1000, countryGeometry.getEnvelopeInternal)

    locationsLevel1.foreach { locationLevel1 =>
      val geometryLevel1 = readGeometry(locationLevel1)
      imageWriter.fill(geometryLevel1, new Color(255, 255, 0, 24))
      locationLevel1.childLocations.foreach { locationLevel2 =>
        locationLevel2.childLocations.foreach { locationLevel3 =>
          val geometryLevel3 = readGeometry(locationLevel3)
          imageWriter.draw(geometryLevel3, new Color(0, 198, 198), 1)
        }
        val geometryLevel2 = readGeometry(locationLevel2)
        imageWriter.draw(geometryLevel2, new Color(198, 198, 198), 1)
      }
      imageWriter.draw(geometryLevel1, Color.red, 1)
    }

    imageWriter.draw(countryGeometry, Color.blue, 1)
    imageWriter.write(filename)
  }

  def printFrance(country: LocationTree, filename: String): Unit = {

    val departments = country.childLocations
    val cdcs = departments.flatMap(_.childLocations).filter(_.name.startsWith("fr-2")).distinct
    val departmentCommunes = departments.flatMap(_.childLocations).filter(_.name.startsWith("fr-3"))

    val countryGeometry = readGeometry(country)
    val imageWriter = new GeometryImageWriter(3000, 3000, countryGeometry.getEnvelopeInternal)

    cdcs.foreach { cdc =>
      val cdcGeometry = readGeometry(cdc)
      imageWriter.fill(cdcGeometry, new Color(255, 255, 0, 36))
      cdc.childLocations.foreach { commune =>
        val communeGeometry = readGeometry(commune)
        imageWriter.draw(communeGeometry, new Color(0, 0, 0, 24), 1)
      }
      imageWriter.draw(cdcGeometry, new Color(0, 0, 128, 128), 2)
    }

    departmentCommunes.foreach { commune =>
      val geometry = readGeometry(commune)
      imageWriter.fill(geometry, new Color(0, 255, 255, 24))
      imageWriter.draw(geometry, new Color(0, 0, 0, 24), 1)
    }

    departments.foreach { department =>
      val geometry = readGeometry(department)
      imageWriter.draw(geometry, Color.red, 2)
    }

    imageWriter.draw(countryGeometry, Color.blue, 1)
    imageWriter.write(filename)
  }

  private def readGeometry(tree: LocationTree): Geometry = {
    locationDatas.find(_.id == tree.name).get.geometry.geometry
  }
}
