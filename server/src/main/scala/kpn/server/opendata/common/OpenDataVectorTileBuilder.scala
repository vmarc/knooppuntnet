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
      val point: Point = geometryFactory.createPoint(new Coordinate(tile.scaleLon(node.lon), tile.scaleLat(node.lat)))

      val userData = Seq(
        "id" -> node._id,
        "name" -> node.name,
      ).toMap

      encoder.addPointFeature("node", userData, point)
    }

    routes.foreach { route =>
      val coordinates = route.coordinates.map { p =>
        new Coordinate(tile.scaleLon(p.lon), tile.scaleLat(p.lat))
      }
      val lineString = geometryFactory.createLineString(coordinates.toArray)
      val userData = Seq(
        "id" -> route._id,
      ).toMap
      encoder.addLineStringFeature("route", userData, lineString)
    }

    encoder.encode
  }
}
