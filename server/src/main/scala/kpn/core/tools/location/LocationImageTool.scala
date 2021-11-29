package kpn.core.tools.location

import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.location.LocationTree
import kpn.server.json.Json
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.Geometry

import java.awt.Color
import java.io.File

object LocationImageTool {
  def main(args: Array[String]): Unit = {
    new LocationImageTool().printBe()
  }
}

class LocationImageTool() {

  private val log = Log(classOf[LocationImageTool])

  def printNl(): Unit = {
    printCountry("nl", "/kpn/locations/netherlands.png")
  }

  def printBe(): Unit = {
    printCountry("be", "/kpn/locations/belgium.png")
  }

  private def printCountry(domain: String, filename: String): Unit = {
    val tree = loadTree(domain)
    val country = tree.children.get.head
    val provinces = country.children.get

    val countryGeometry = loadGeometry(domain, domain)
    val imageWriter = new GeometryImageWriter(1000, 1000, countryGeometry.getEnvelopeInternal)

    provinces.foreach { province =>
      val provinceGeometry = loadGeometry(domain, province.name)
      imageWriter.fill(provinceGeometry, new Color(255, 255, 0, 24))
      province.childLocations.foreach { municipality =>
        val municipalityGeometry = loadGeometry(domain, municipality.name)
        imageWriter.draw(municipalityGeometry, new Color(0, 0, 0, 24), 1)
      }
      imageWriter.draw(provinceGeometry, Color.red, 1)
    }

    imageWriter.draw(countryGeometry, Color.blue, 1)
    imageWriter.write(filename)
  }

  def print(): Unit = {
    val tree = loadTree("fr")
    val country = tree.children.get.head
    val departments = country.children.get
    val cdcs = departments.flatMap(_.childLocations).filter(_.name.startsWith("fr-2")).distinct
    val departmentCommunes = departments.flatMap(_.childLocations).filter(_.name.startsWith("fr-3"))

    val countryGeometry = loadGeometry("fr", "fr")
    val imageWriter = new GeometryImageWriter(3000, 3000, countryGeometry.getEnvelopeInternal)

    cdcs.foreach { cdc =>
      val cdcGeometry = loadGeometry("fr", cdc.name)
      imageWriter.fill(cdcGeometry, new Color(255, 255, 0, 36))
      cdc.childLocations.foreach { commune =>
        val communeGeometry = loadGeometry("fr", commune.name)
        imageWriter.draw(communeGeometry, new Color(0, 0, 0, 24), 1)
      }
      imageWriter.draw(cdcGeometry, new Color(0, 0, 128, 128), 2)
    }

    departmentCommunes.foreach { commune =>
      val geometry = loadGeometry("fr", commune.name)
      imageWriter.fill(geometry, new Color(0, 255, 255, 24))
      imageWriter.draw(geometry, new Color(0, 0, 0, 24), 1)
    }

    departments.foreach { department =>
      val geometry = loadGeometry("fr", department.name)
      imageWriter.draw(geometry, Color.red, 2)
    }

    imageWriter.draw(countryGeometry, Color.blue, 1)
    imageWriter.write(s"/kpn/locations/france.png")
  }

  private def loadTree(country: String): LocationTree = {
    val filename = s"/kpn/locations/$country/tree.json"
    val string = FileUtils.readFileToString(new File(filename), "UTF-8")
    Json.objectMapper.readValue(string, classOf[LocationTree])
  }

  private def loadGeometry(country: String, id: String): Geometry = {
    val filename = s"/kpn/locations/$country/geometries/$id.json"
    val string = FileUtils.readFileToString(new File(filename), "UTF-8")
    Json.objectMapper.readValue(string, classOf[Geometry])
  }
}
