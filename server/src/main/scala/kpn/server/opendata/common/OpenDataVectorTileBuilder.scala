package kpn.server.opendata.common

import kpn.server.analyzer.engine.tiles.domain.OldTile
import kpn.server.analyzer.engine.tiles.vector.encoder.VectorTileEncoder
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point

class OpenDataVectorTileBuilder {

  def build(tile: OldTile, nodes: Seq[OpenDataNode], routes: Seq[OpenDataRoute]): Array[Byte] = {

    val geometryFactory = new GeometryFactory

    val encoder = new VectorTileEncoder()

    nodes.foreach { node =>
      val point: Point = geometryFactory.createPoint(new Coordinate(tile.scaleLon(node.lon), tile.scaleLat(node.lat)))

      val userData = Seq(
        Some("id" -> node._id),
        Some("name" -> node.name),
        if (node.virtual) Some("virtual" -> "true") else None
      ).flatten.toMap

      encoder.addPointFeature("opendata-node", userData, point)
    }

    routes.foreach { route =>
      val coordinates = route.coordinates.map { p =>
        new Coordinate(tile.scaleLon(p.lon), tile.scaleLat(p.lat))
      }
      val lineString = geometryFactory.createLineString(coordinates.toArray)
      val userData = Seq(
        Some("id" -> route._id),
        if (route.virtual) Some("virtual" -> "true") else None
      ).flatten.toMap
      encoder.addLineStringFeature("opendata-route", userData, lineString)
    }

    encoder.encode
  }
}
