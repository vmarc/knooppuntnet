package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.Country
import kpn.api.common.Language
import kpn.core.tools.config.Dirs
import kpn.core.tools.location.LocationNameDefinition
import kpn.core.tools.location.LocationNameDefinitions
import kpn.core.util.Log
import kpn.server.json.Json
import org.apache.commons.io.FileUtils

import java.io.File

class LocationConfigurationReader {

  private val log = Log(classOf[LocationConfigurationReader])

  def read(): LocationConfiguration = {
    val rootLocations = Country.values.map { country =>
      log.info(s"Loading ${country.entryName.toUpperCase}")
      val locationNameDefinitions = {
        val filename = s"${Dirs.root}/locations/${country.entryName}/locations.json"
        val string = FileUtils.readFileToString(new File(filename), "UTF-8")
        Json.objectMapper.readValue(string, classOf[LocationNameDefinitions])
      }
      val locationMap = locationNameDefinitions.locations.map(lnd => lnd.id -> lnd).toMap
      val treeFilename = s"${Dirs.root}/locations/${country.entryName}/tree.json"
      val string = FileUtils.readFileToString(new File(treeFilename), "UTF-8")
      val tree = Json.objectMapper.readValue(string, classOf[LocationTree])
      toLocation(locationMap, tree)
    }

    LocationConfiguration(rootLocations)
  }

  private def toLocation(locationDefinitionMap: Map[String, LocationNameDefinition], tree: LocationTree): LocationDefinition = {
    val children = if (tree.children.isEmpty) {
      Seq.empty
    }
    else {
      tree.children.get.map(child => toLocation(locationDefinitionMap, child))
    }

    locationDefinitionMap.get(tree.name) match {
      case None => throw new RuntimeException("xxx")
      case Some(locationNameDefinition) =>
        val names: Map[Language, String] = locationNameDefinition.names match {
          case None => Map.empty
          case Some(locationNames) =>
            locationNames.map { locationName =>
              locationName.language -> locationName.name
            }.toMap
        }
        LocationDefinition(
          locationNameDefinition.id,
          locationNameDefinition.relationId,
          locationNameDefinition.name,
          names,
          locationNameDefinition.tags,
          children
        )
    }
  }
}
