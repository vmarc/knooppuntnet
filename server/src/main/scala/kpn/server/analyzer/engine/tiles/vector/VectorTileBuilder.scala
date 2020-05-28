package kpn.server.analyzer.engine.tiles.vector

import kpn.server.analyzer.engine.tiles.TileBuilder
import kpn.server.analyzer.engine.tiles.TileData
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.vector.encoder.VectorTileEncoder
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point

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

      encoder.addPointFeature(node.layer, userData, point)
    }

    data.routes.zipWithIndex.foreach { case (tileRoute, index) =>
      tileRoute.segments.foreach { segment =>
        val coordinates = segment.lines.flatMap { line =>
          Seq(
            new Coordinate(scaleLon(line.p1.x), scaleLat(line.p1.y)),
            new Coordinate(scaleLon(line.p2.x), scaleLat(line.p2.y))
          )
        }
        val lineString = geomFactory.createLineString(coordinates.toArray)
        val userData: ListMap[String, String] = tileRoute.surveyDate match {
          case Some(surveyDate) =>
            ListMap(
              "id" -> (tileRoute.routeId.toString + "-" + index),
              "name" -> tileRoute.routeName,
              "surface" -> segment.surface,
              "survey" -> surveyDate.yyyymm
            )
          case None =>
            ListMap(
              "id" -> (tileRoute.routeId.toString + "-" + index),
              "name" -> tileRoute.routeName,
              "surface" -> segment.surface
            )
        }
        encoder.addLineStringFeature(tileRoute.layer, userData, lineString)
      }
    }

    encoder.encode
  }
}
