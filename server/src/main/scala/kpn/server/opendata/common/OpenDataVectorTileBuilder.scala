package kpn.server.opendata.common

import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.vector.encoder.VectorTileEncoder
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point

class OpenDataVectorTileBuilder {

  def build(tile: Tile, nodes: Seq[OpenDataNode], routes: Seq[OpenDataRoute]): Array[Byte] = {

    val geometryFactory = new GeometryFactory

    val encoder = new VectorTileEncoder()

    nodes.foreach { node =>
      val point: Point = geometryFactory.createPoint(tile.scale(new Coordinate(node.lon, node.lat)))

      val userData = Seq(
        "id" -> node._id,
        "name" -> node.name,
      ).toMap

      encoder.addPointFeature("opendata-node", userData, point)
    }

    routes.foreach { route =>
      val coordinates = tile.scale(route.coordinates.map(p => new Coordinate(p.lon, p.lat)))
      val lineString = geometryFactory.createLineString(coordinates.toArray)
      val userData = Seq(
        "id" -> route._id,
      ).toMap
      encoder.addLineStringFeature("opendata-route", userData, lineString)
    }

    encoder.encode
  }
}
