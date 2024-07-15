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
      val coordinate = data.tile.scale(new Coordinate(node.lon, node.lat))
      val point = geometryFactory.createPoint(coordinate)

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
        val coordinates = segment.lineSegments.flatMap { line =>
          Seq(
            data.tile.scale(new Coordinate(line.p0.x, line.p0.y)),
            data.tile.scale(new Coordinate(line.p1.x, line.p1.y)),
          )
        }
        val lineString = geometryFactory.createLineString(coordinates.toArray)
        val userData = Seq(
          Some("routeId" -> tileRoute.routeId.toString),
          Some("segmentId" -> segment.segmentId.toString),
          Some("segmentElementId" -> segment.segmentElementId.toString),
          Some("pathIds" -> segment.pathIds.mkString(",")),
          Some("name" -> tileRoute.routeName),
          // TODO redesign - Some("oneway" -> segment.oneWay.toString),
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
