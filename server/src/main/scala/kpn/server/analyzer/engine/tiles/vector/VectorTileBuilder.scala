package kpn.server.analyzer.engine.tiles.vector

import kpn.core.util.Log
import kpn.server.analyzer.engine.tiles.TileBuilder
import kpn.server.analyzer.engine.tiles.TileData
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.vector.encoder.VectorTileEncoder
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier

class VectorTileBuilder extends TileBuilder {

  private val log = Log(classOf[VectorTileBuilder])
  private val geometryFactory = new GeometryFactory

  def build(data: TileData, tile: Tile): Array[Byte] = {

    val encoder = new VectorTileEncoder()

    data.nodes.foreach { node =>
      val coordinate = tile.scale(new Coordinate(node.lon, node.lat))
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

    data.routes.zipWithIndex.foreach { case (tileRoute, index) =>
      if (data.routes.size > 50) {
        log.info(s"${index + 1}/${data.routes.size} route ${tileRoute.routeName}")
      }
      tileRoute.segments.foreach { segment =>
        if (tile.contains(segment.worldCoordinates)) {
          val scaledCoordinates = segment.worldCoordinates.sliding(2, 2).map { case Seq(x, y) =>
            tile.scale(new Coordinate(x, y))
          }
          val lineString = geometryFactory.createLineString(scaledCoordinates.toArray)
          val simplifiedLineString: LineString = if (tile.z < 14) {
            DouglasPeuckerSimplifier.simplify(lineString, 3).asInstanceOf[LineString]
          }
          else {
            lineString
          }

          // TODO redesign tiles - cleanup:
          // log.info(s"simplication, before=${lineString.getNumPoints}, after=${simplifiedLineString.getNumPoints}")

          val userData = Seq(
            Some("routeId" -> tileRoute.routeId.toString),
            Some("segmentId" -> segment.segmentId.toString),
            Some("segmentElementId" -> segment.segmentElementId.toString),
            Some("pathIds" -> segment.pathIds.mkString(",")),
            Some("name" -> tileRoute.routeName),
            // TODO redesign tiles - Some("oneway" -> segment.oneWay.toString),
            Some("surface" -> segment.surface),
            tileRoute.surveyDate.map(surveyDate => "survey" -> surveyDate.yyyymm),
            tileRoute.state.map(state => "state" -> state)
          ).flatten.toMap
          encoder.addLineStringFeature(tileRoute.layer, userData, simplifiedLineString)
        }
      }
    }

    encoder.encode
  }
}
