package kpn.server.analyzer.engine.tiles.vector

import kpn.server.analyzer.engine.tiles.TileBuilder
import kpn.server.analyzer.engine.tiles.TileData
import kpn.server.analyzer.engine.tiles.vector.encoder.VectorTileEncoder
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory

class VectorTileBuilder extends TileBuilder {

  def build(data: TileData): Array[Byte] = {

    val geometryFactory = new GeometryFactory

    val encoder = new VectorTileEncoder()

    data.nodes.foreach { node =>
      val x = data.tile.scaleLon(node.lon)
      val y = data.tile.scaleLat(node.lat)
      val point = geometryFactory.createPoint(new Coordinate(x, y))

      val userData = Seq(
        Some("id" -> node.id.toString),
        node.ref.map(ref => "ref" -> ref),
        node.name.map(name => "name" -> name),
        node.surveyDate.map(surveyDate => "survey" -> surveyDate.yyyymm),
        if (node.proposed) Some("proposed" -> "true") else None
      ).flatten.toMap

      encoder.addPointFeature(node.layer, userData, point)
    }

    data.routes.foreach { tileRoute =>
      tileRoute.segments.foreach { segment =>
        val coordinates = segment.lines.flatMap { line =>
          Seq(
            new Coordinate(data.tile.scaleLon(line.p1.x), data.tile.scaleLat(line.p1.y)),
            new Coordinate(data.tile.scaleLon(line.p2.x), data.tile.scaleLat(line.p2.y))
          )
        }
        val lineString = geometryFactory.createLineString(coordinates.toArray)
        val userData = Seq(
          Some("id" -> (tileRoute.routeId.toString + "-" + segment.pathId)),
          Some("name" -> tileRoute.routeName),
          Some("oneway" -> segment.oneWay.toString),
          Some("surface" -> segment.surface),
          tileRoute.surveyDate.map(surveyDate => "survey" -> surveyDate.yyyymm),
          tileRoute.state.map(state => "state" -> state)
        ).flatten.toMap
        encoder.addLineStringFeature(tileRoute.layer, userData, lineString)
      }
    }

    encoder.encode
  }
}
