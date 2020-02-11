package kpn.server.analyzer.engine.analysis.location

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

  def read(): LocationConfiguration = {
    val string = FileUtils.readFileToString(LocationConfigurationDefinition.treeFile, "UTF-8")
    val root = Json.objectMapper.readValue(string, classOf[LocationTree])
    val configuration = LocationConfiguration(root.children.toSeq.flatten.map(toLocationConfiguration))
    val deDuplicatedConfiguration = new LocationConfigurationDeDuplicator().deduplicate(configuration)
    new LocationConfigurationValidator().validate(deDuplicatedConfiguration)
    deDuplicatedConfiguration
  }

  private def toLocationConfiguration(tree: LocationTree): LocationDefinition = {
    val children = tree.children.toSeq.flatten.map(toLocationConfiguration)
    val file = LocationConfigurationDefinition.file(tree.name)
    new LocationDefinitionReader(file).read(children)
  }
}
