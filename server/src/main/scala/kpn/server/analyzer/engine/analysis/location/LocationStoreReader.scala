package kpn.server.analyzer.engine.analysis.location

import kpn.api.custom.Country
import kpn.core.tools.location.LocationGeometry
import kpn.core.tools.location.LocationNameDefinitions
import kpn.core.util.Log
import kpn.server.json.Json
import org.apache.commons.io.FileUtils
import org.locationtech.jts.algorithm.locate.IndexedPointInAreaLocator
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.MultiPolygon
import org.locationtech.jts.geom.Polygon

import java.io.File

class LocationStoreReader {

  private val log = Log(classOf[LocationStoreReader])

  private val root = "/kpn/locations"

  def read(): LocationStore = {
    val countries: Seq[LocationStoreCountry] = Country.all.map { country =>
      val locationStoreCountry = loadCountry(country)
      val filename = s"$root/${country.domain}/tree.json"
      val string = FileUtils.readFileToString(new File(filename), "UTF-8")
      val tree = Json.objectMapper.readValue(string, classOf[LocationTree])
      val location = toLocation(locationStoreCountry.dataMap, tree)
      locationStoreCountry.copy(tree = location)
    }
    LocationStore(countries)
  }

  private def loadCountry(country: Country): LocationStoreCountry = {
    log.info("Loading " + country.domain.toUpperCase)
    val locationNameDefinitions = {
      val filename = s"$root/${country.domain}/locations.json"
      val string = FileUtils.readFileToString(new File(filename), "UTF-8")
      Json.objectMapper.readValue(string, classOf[LocationNameDefinitions])
    }
    val dataMap = locationNameDefinitions.locations.map { locationNameDefinition =>
      val locationGeometry = {
        val filename = s"$root/${country.domain}/geometries/${locationNameDefinition.id}.json"
        val string = FileUtils.readFileToString(new File(filename), "UTF-8")
        val geometry = Json.objectMapper.readValue(string, classOf[Geometry])
        LocationGeometry(geometry)
      }
      val locators = toPolygons(locationGeometry.geometry).map(polygon => new IndexedPointInAreaLocator(polygon))
      locationNameDefinition.id -> LocationStoreData(
        locationNameDefinition.id,
        locationNameDefinition.paths,
        locationNameDefinition.name,
        locationGeometry,
        locators
      )
    }.toMap

    LocationStoreCountry(
      country,
      dataMap,
      dataMap(country.domain)
    )
  }

  private def toPolygons(geometry: Geometry): Seq[Polygon] = {
    geometry match {
      case polygon: Polygon =>
        Seq(polygon)

      case multiPolygon: MultiPolygon =>
        0.until(multiPolygon.getNumGeometries).map { index =>
          multiPolygon.getGeometryN(index) match {
            case polygon: Polygon => polygon
            case _ => throw new RuntimeException("Unexpected: non-polygon geometry in multipolygon")
          }
        }

      case geometryCollection: GeometryCollection =>
        0.until(geometryCollection.getNumGeometries).map { index =>
          geometryCollection.getGeometryN(index) match {
            case polygon: Polygon => polygon
            case _ => throw new RuntimeException("Unexpected: non-polygon geometry in geometry collection")
          }
        }

      case _ => throw new RuntimeException("Unexpected location geometry type")
    }
  }

  private def toLocation(locationDefinitionMap: Map[String, LocationStoreData], tree: LocationTree): LocationStoreData = {
    if (tree.children.isEmpty) {
      locationDefinitionMap(tree.name)
    }
    else {
      locationDefinitionMap(tree.name).copy(
        children = tree.children.get.map(child =>
          toLocation(locationDefinitionMap, child)
        )
      )
    }
  }
}
