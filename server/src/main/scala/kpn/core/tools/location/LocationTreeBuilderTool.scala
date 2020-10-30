package kpn.core.tools.location

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
      val countryLocations = readLocations(configuration.country)
      new LocationTreeBuilder(configuration, countryLocations).countryLocation()
    }
    LocationConfiguration(locations)
  }

  private def readLocations(country: Country): Seq[LocationDefinition] = {
    val locations = new LocationDefinitionReader(LocationConfigurationDefinition.DIR, country).read()
    locations.filterNot(location => LocationConfigurationDefinition.skippedLocations.contains(location.id))
  }

  private def writeTree(configuration: LocationConfiguration): Unit = {
    log.info("Write tree")
    val tree = LocationTree("root", Some(configuration.locations.map(toTree)))
    val file = LocationConfigurationDefinition.treeFile
    Json.objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, tree)
  }

  private def toTree(location: LocationDefinition): LocationTree = {
    if (location.children.size > 1) {
      LocationTree(location.id, Some(location.children.map(toTree)))
    }
    else {
      LocationTree(location.id)
    }
  }
}
