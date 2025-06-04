package kpn.server.opendata.common

import kpn.api.common.FeatureLayer
import kpn.server.analyzer.engine.tile.Feature
import kpn.server.analyzer.engine.tile.TileEncoder
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileContext
import kpn.server.analyzer.engine.tiles.domain.TileUtil
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.Point

class OpenDataVectorTileBuilder(tileContext: TileContext, tile: Tile, nodes: Seq[OpenDataNode], routes: Seq[OpenDataRoute]) {

  private val geometryFactory = new GeometryFactory

  def build(): Array[Byte] = {
    val features = nodeFeatures() ++ routeFeatures()
    TileEncoder.encode(tileContext, features)
  }

  private def nodeFeatures(): Seq[Feature] = {
    nodes.map { node =>
      val point = nodePoint(node)
      val userData = nodeUserData(node)
      Feature(FeatureLayer.opendataNode, userData, point)
    }
  }

  private def routeFeatures(): Seq[Feature] = {
    routes.flatMap { route =>
      val lineString = routeLineString(route)
      if (includeLineString(lineString)) {
        val userData = routeUserData(route)
        Some(Feature(FeatureLayer.opendataRoute, userData, lineString))
      }
      else {
        None
      }
    }
  }

  private def nodePoint(node: OpenDataNode): Point = {
    val worldCoordinate = new Coordinate(lonToWorldX(node.lon), latToWorldY(node.lat))
    val scaledCoordinate = tileContext.toTileCoorinate(tile, worldCoordinate)
    val coordinate = new Coordinate(Math.floor(scaledCoordinate.x), Math.floor(scaledCoordinate.y))
    geometryFactory.createPoint(coordinate)
  }

  private def nodeUserData(node: OpenDataNode): Map[String, String] = {
    Seq(
      Some("id" -> node._id),
      Some("name" -> node.name),
      if (node.virtual) Some("virtual" -> "true") else None
    ).flatten.toMap
  }

  private def includeLineString(lineString: LineString): Boolean = {
    val length = lineString.getLength
    val minLength = if (tile.z < 7) 1 else 1.5
    length >= minLength
  }

  private def routeLineString(route: OpenDataRoute) = {
    val worldCoordinates = route.coordinates.map(coordinate => new Coordinate(lonToWorldX(coordinate.lon), latToWorldY(coordinate.lat)))
    val tileCoordinates = TileUtil.tileCoordinates(tileContext, tile, worldCoordinates)
    val coordinates = tileCoordinates.map(c => new Coordinate(c.x, c.y))
    geometryFactory.createLineString(coordinates.toArray)
  }

  private def routeUserData(route: OpenDataRoute): Map[String, String] = {
    if (route.virtual) {
      Map("virtual" -> "true")
    } else {
      Map.empty
    }
  }
}
