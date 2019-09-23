package kpn.core.engine.analysis.location

import java.io.File
import java.io.FileFilter

object LocationsReader {

  def main(args: Array[String]): Unit = {

    def printLocation(indent: Int, location: LocationDefinition): Unit = {
      val spaces = (0 to indent).map(x => "  ").mkString
      val children = if (location.children.isEmpty) "" else "  (" + location.children.size + ")"
      println(spaces + location.name + " " + location.names.mkString(",") + children)
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
    Seq(LocationConfiguration.be).flatMap { configuration =>
      val locationsPerLevel = configuration.levels.map { level =>
        readLocations(configuration.country, level)
      }
      readLevelLocations(locationsPerLevel.head, locationsPerLevel.tail)
    }
  }

  private def readLevelLocations(levelLocations: Seq[LocationDefinition], remainderLocations: Seq[Seq[LocationDefinition]]): Seq[LocationDefinition] = {
    if (remainderLocations.isEmpty) {
      levelLocations
    }
    else {
      val nextLevelLocations = remainderLocations.head
      levelLocations.map { location =>
        val children = nextLevelLocations.filter(loc => location.geometry.contains(loc.geometry))
        val populatedChildren = readLevelLocations(children, remainderLocations.tail)
        location.copy(children = populatedChildren)
      }
    }
  }

  private def readLocations(country: String, level: String): Seq[LocationDefinition] = {
    val root = new File("/kpn/conf/locations/" + country)
    root.listFiles(new GeoJsonFileFilter(level)).map { file =>
      new LocationDefinitionReader(file).read(Seq.empty)
    }
  }

}
