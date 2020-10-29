package kpn.server.analyzer.engine.analysis.location

import kpn.api.custom.Country
import kpn.core.util.Log
import kpn.server.json.Json
import org.apache.commons.io.FileUtils

object LocationConfigurationReader {

  def main(args: Array[String]): Unit = {

    def printLocation(indent: Int, location: LocationDefinition): Unit = {
      val area = (location.children.map(_.geometry.getArea).sum * 1000).toInt
      val spaces = (0 to indent).map(x => "  ").mkString
      val children = if (location.children.isEmpty) {
        " area=" + (location.geometry.getArea * 1000).toInt
      }
      else {
        "  (" + location.children.size + ")" + " area=" + (location.geometry.getArea * 1000).toInt + "/" + area
      }
      println(spaces + location.name + " " + location.names.mkString(",") + children)
      location.children.foreach { child =>
        printLocation(indent + 1, child)
      }
    }

    val locationConfiguration = new LocationConfigurationReader().read()
    locationConfiguration.locations.foreach { location =>
      printLocation(0, location)
    }
  }
}

class LocationConfigurationReader {

  private val log = Log(classOf[LocationConfigurationReader])

  def read(): LocationConfiguration = {
    val string = FileUtils.readFileToString(LocationConfigurationDefinition.treeFile, "UTF-8")
    val root = Json.objectMapper.readValue(string, classOf[LocationTree])
    val locationDefinitionMap = loadLocationDefinitions().map(d => d.id -> d).toMap
    val rootLocations = root.children.get.map(treeElement => toLocation(locationDefinitionMap, treeElement))
    val configuration = LocationConfiguration(rootLocations)
    val deDuplicatedConfiguration = new LocationConfigurationDeDuplicator().deduplicate(configuration)
    new LocationConfigurationValidator().validate(deDuplicatedConfiguration)
    deDuplicatedConfiguration
  }

  private def toLocation(locationDefinitionMap: Map[String, LocationDefinition], tree: LocationTree): LocationDefinition = {
    if (tree.children.isEmpty) {
      locationDefinitionMap(tree.name)
    }
    else {
      locationDefinitionMap(tree.name).copy(children = tree.children.get.map(child => toLocation(locationDefinitionMap, child)))
    }
  }

  private def loadLocationDefinitions(): Seq[LocationDefinition] = {
    Country.all.flatMap { country =>
      log.info(s"Loading ${country.domain.toUpperCase} location definitions")
      new LocationDefinitionReader(LocationConfigurationDefinition.DIR, country).read()
    }
  }

}
