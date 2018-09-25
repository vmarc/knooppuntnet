package kpn.core.tiles.vector

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.Point
import kpn.core.tiles.TileBuilder
import kpn.core.tiles.TileData
import kpn.core.tiles.domain.Tile
import kpn.core.tiles.vector.encoder.VectorTileEncoder

import scala.collection.immutable.ListMap

class VectorTileBuilder() extends TileBuilder {

  def build(data: TileData): Array[Byte] = {

    val geomFactory = new GeometryFactory

    val encoder = new VectorTileEncoder()

    def scaleLat(lat: Double): Double = {
      Tile.EXTENT - ((lat - data.tile.bounds.yMin) * Tile.EXTENT / (data.tile.bounds.yMax - data.tile.bounds.yMin))
    }

    def scaleLon(lon: Double): Double = {
      (lon - data.tile.bounds.xMin) * Tile.EXTENT / (data.tile.bounds.xMax - data.tile.bounds.xMin)
    }

    data.nodes.foreach { node =>
      val point: Point = geomFactory.createPoint(new Coordinate(scaleLon(node.lon), scaleLat(node.lat)))

      val userData = ListMap(
        "id" -> node.id.toString,
        "name" -> node.name
      )

      val layer = if (!node.definedInRelation && !node.definedInRelation && node.routeReferences.isEmpty) {
        if (node.integrityCheck.isDefined && node.integrityCheck.get.failed) {
          "error-orphan-node"
        }
        else {
          "orphan-node"
        }
      }
      else if (node.integrityCheck.isDefined && node.integrityCheck.get.failed) {
        "error-node"
      }
      else {
        "node"
      }

      encoder.addPointFeature(layer, userData, point)
    }

    data.routes.zipWithIndex.foreach { case (tileRoute, index) =>
      val lineStrings = tileRoute.segments.map { segment =>
        val coordinates = segment.lines.flatMap { line =>
          Seq(
            new Coordinate(scaleLon(line.p1.x), scaleLat(line.p1.y)),
            new Coordinate(scaleLon(line.p2.x), scaleLat(line.p2.y))
          )
        }
        geomFactory.createLineString(coordinates.toArray)
      }

      val userData = ListMap(
        "id" -> (tileRoute.routeId.toString + "-" + index),
        "name" -> tileRoute.routeName
      )

      val geometry = geomFactory.createMultiLineString(lineStrings.toArray)
      encoder.addMultiLineStringFeature(tileRoute.layer, userData, geometry)
    }

    encoder.encode
  }
}
