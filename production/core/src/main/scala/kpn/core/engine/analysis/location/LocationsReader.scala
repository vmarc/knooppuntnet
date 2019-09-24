package kpn.core.engine.analysis.location

import java.io.File
import java.io.FileFilter

object LocationsReader {

  def main(args: Array[String]): Unit = {

    def printLocation(indent: Int, location: LocationDefinition): Unit = {
      val area = (location.children.map(_.area).sum * 1000).toInt
      val spaces = (0 to indent).map(x => "  ").mkString
      val children = if (location.children.isEmpty) {
        " area=" + (location.area * 1000).toInt
      }
      else {
        "  (" + location.children.size + ")" + " area=" + (location.area * 1000).toInt + "/" + area
      }
      println(spaces + location.level + " " + location.name + " " + location.names.mkString(",") + children)
      location.children.foreach { child =>
        printLocation(indent + 1, child)
      }
    }

    val locations = new LocationsReader().read()
    locations.foreach { location =>
      printLocation(0, location)
    }
  }
}

class LocationsReader {

  class GeoJsonFileFilter(level: String) extends FileFilter {
    def accept(pathname: File): Boolean = {
      pathname.getName.endsWith("_" + level + ".GeoJson")
    }
  }

  def read(): Seq[LocationDefinition] = {
    println("loading location definitions")

    val locations = LocationConfiguration.countries.flatMap { configuration =>
      println("loading location definitions " + configuration.country)
      val locationsPerLevel = configuration.levels.map { level =>
        readLocations(configuration.country, level)
      }
      readLevelLocations(configuration.country, locationsPerLevel.head, locationsPerLevel.tail)
    }
    println("loading location definitions done")
    locations
  }

  private def readLevelLocations(country: String, levelLocations: Seq[LocationDefinition], remainderLocations: Seq[Seq[LocationDefinition]]): Seq[LocationDefinition] = {
    if (remainderLocations.isEmpty) {
      levelLocations
    }
    else {
      val nextLevelLocations = remainderLocations.head
      levelLocations.map { location =>
        val children = nextLevelLocations.filter(loc => location.geometry.contains(loc.geometry))
        if (country == "be" && location.level == 4 && children.isEmpty) {
          val nextLevelLocations2 = remainderLocations(2) // level 8 Brussels-Capital
          val children2 = nextLevelLocations2.filter(loc => location.geometry.contains(loc.geometry))
          location.copy(children = children2)
        }
        else if (country == "de" && location.level == 4 && children.isEmpty) {
          val nextLevelLocations2 = remainderLocations.tail.head
          val children2 = nextLevelLocations2.filter(loc => location.geometry.contains(loc.geometry))
          location.copy(children = children2)
        }
        else {
          val populatedChildren = readLevelLocations(country, children, remainderLocations.tail)
          location.copy(children = populatedChildren)
        }
      }
    }
  }

  private def readLocations(country: String, level: Int): Seq[LocationDefinition] = {
    val root = new File("/kpn/conf/locations/" + country)
    root.listFiles(new GeoJsonFileFilter("AL" + level)).map { file =>
      new LocationDefinitionReader(file).read(level, Seq.empty)
    }
  }

}
