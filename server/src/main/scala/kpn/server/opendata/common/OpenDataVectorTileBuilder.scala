package kpn.server.opendata.common

import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileUtil
import no.ecc.vectortile.VectorTileEncoder
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point

import scala.jdk.CollectionConverters.MapHasAsJava

class OpenDataVectorTileBuilder {

  private val geometryFactory = new GeometryFactory

  def build(tile: Tile, nodes: Seq[OpenDataNode], routes: Seq[OpenDataRoute]): Array[Byte] = {

    val encoder = new VectorTileEncoder(tile.extent, tile.clipBufferSize, false)

    encodeNodes(encoder, tile, nodes)
    encodeRoutes(encoder, tile, routes)
    encoder.encode
  }

  private def encodeNodes(encoder: VectorTileEncoder, tile: Tile, nodes: Seq[OpenDataNode]): Unit = {
    nodes.foreach { node =>
      val worldCoordinate = new Coordinate(lonToWorldX(node.lon), latToWorldY(node.lat))
      val scaledCoordinate = tile.scale(worldCoordinate)
      val coordinate = new Coordinate(Math.floor(scaledCoordinate.x), Math.floor(scaledCoordinate.y))
      val point: Point = geometryFactory.createPoint(coordinate)

      val userData = Seq(
        Some("id" -> node._id),
        Some("name" -> node.name),
        if (node.virtual) Some("virtual" -> "true") else None
      ).flatten.toMap.asJava

      encoder.addFeature("opendata-node", userData, point)
    }
  }

  private def encodeRoutes(encoder: VectorTileEncoder, tile: Tile, routes: Seq[OpenDataRoute]): Unit = {
    routes.foreach { route =>
      val worldCoordinates = route.coordinates.map(coordinate => new Coordinate(lonToWorldX(coordinate.lon), latToWorldY(coordinate.lat)))
      val tileCoordinates = TileUtil.tileCoordinates(tile, worldCoordinates)
      val coordinates = tileCoordinates.map(c => new Coordinate(c.x, c.y))
      val lineString = geometryFactory.createLineString(coordinates.toArray)

      val userData = Seq(
        Some("id" -> route._id),
        if (route.virtual) Some("virtual" -> "true") else None
      ).flatten.toMap.asJava

      encoder.addFeature("opendata-route", userData, lineString)
    }
  }
}
