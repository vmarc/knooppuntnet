package kpn.core.tools.location

import java.io.File
import java.io.FileFilter

import kpn.api.custom.Country
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.location.LocationConfiguration
import kpn.server.analyzer.engine.analysis.location.LocationConfigurationDefinition
import kpn.server.analyzer.engine.analysis.location.LocationDefinition
import kpn.server.analyzer.engine.analysis.location.LocationDefinitionReader
import kpn.server.analyzer.engine.analysis.location.LocationTree
import kpn.server.json.Json

/*
  Generates the location tree configuration file.
 */
object LocationTreeBuilderTool {
  def main(args: Array[String]): Unit = {
    new LocationTreeBuilderTool().run()
  }
}

class LocationTreeBuilderTool {

  private val log = Log(classOf[LocationTreeBuilderTool])

  private val areaCache = collection.mutable.Map[String, Double]()

  private class GeoJsonFileFilter(level: String) extends FileFilter {
    def accept(pathname: File): Boolean = {
      pathname.getName.endsWith("_" + level + ".GeoJson")
    }
  }

  def run(): Unit = {
    val configuration = buildConfigurationTree()
    writeTree(configuration)
    log.info("Done")
  }

  private def buildConfigurationTree(): LocationConfiguration = {
    log.info("Load configuration")
    val locations = LocationConfigurationDefinition.countries.flatMap { configuration =>
      val country = configuration.country.domain.toUpperCase
      log.info(s"$country Loading locations")
      val locationsPerLevel = configuration.levels.map { level =>
        readLocations(configuration.country, level)
      }
      log.info(s"$country Calculating hierarchy")
      calculateTree(0, configuration.country, locationsPerLevel.head, locationsPerLevel.tail)
    }
    LocationConfiguration(locations)
  }

  private def calculateTree(depth: Int, country: Country, levelLocations: Seq[LocationDefinition],
                            remainderLocations: Seq[Seq[LocationDefinition]]): Seq[LocationDefinition] = {

    if (remainderLocations.isEmpty) {
      levelLocations
    }
    else {
      val nextLevelLocations = remainderLocations.head
      levelLocations.zipWithIndex.map { case (location, index) =>
        if (depth == 1) {
          log.info(s"${country.domain.toUpperCase} ${index + 1}/${levelLocations.size}  ${location.name} ")
        }
        val children = nextLevelLocations.filter(loc => contains(location, loc))
        if (country == Country.be && depth == 1 && children.isEmpty) {
          val nextLevelLocations2 = remainderLocations(2) // level 8 Brussels-Capital
          val children2 = nextLevelLocations2.filter(loc => contains(location, loc))
          location.copy(children = children2.sortBy(_.name))
        }
        else if (country == Country.de && depth == 1 && children.isEmpty) {
          val nextLevelLocations2 = remainderLocations.tail.head
          val children2 = nextLevelLocations2.filter(loc => contains(location, loc))
          location.copy(children = children2.sortBy(_.name))
        }
        else {
          val populatedChildren = calculateTree(depth + 1, country, children, remainderLocations.tail)
          location.copy(children = populatedChildren.sortBy(_.name))
        }
      }
    }
  }

  private def readLocations(country: Country, level: Int): Seq[LocationDefinition] = {
    val countryRootDir = LocationConfigurationDefinition.DIR + country.domain
    val root = new File(countryRootDir)
    root.listFiles(new GeoJsonFileFilter("AL" + level)).map { file =>
      new LocationDefinitionReader(file).read()
    }
  }

  private def contains(a: LocationDefinition, b: LocationDefinition): Boolean = {
    val intersection = a.geometry.intersection(b.geometry)
    val intersectionArea = intersection.getArea
    val bArea = areaCache.getOrElseUpdate(b.filename, b.geometry.getArea)
    Math.abs(intersectionArea / bArea) > 0.95
  }

  private def writeTree(configuration: LocationConfiguration): Unit = {
    log.info("Write tree")
    val tree = LocationTree("root", Some(configuration.locations.map(toTree)))
    val file = LocationConfigurationDefinition.treeFile
    Json.objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, tree)
  }

  private def toTree(location: LocationDefinition): LocationTree = {
    if (location.children.nonEmpty) {
      LocationTree(location.filename, Some(location.children.map(toTree)))
    }
    else {
      LocationTree(location.filename)
    }
  }

}
