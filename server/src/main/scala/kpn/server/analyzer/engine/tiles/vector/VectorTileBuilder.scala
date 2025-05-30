package kpn.server.analyzer.engine.tiles.vector

import kpn.core.util.Log
import kpn.server.analyzer.engine.tiles.OldTileData
import kpn.server.analyzer.engine.tiles.TileBuilder
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import no.ecc.vectortile.VectorTileEncoder
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier

import scala.jdk.CollectionConverters.MapHasAsJava

class VectorTileBuilder extends TileBuilder {

  private val log = Log(classOf[VectorTileBuilder])
  private val geometryFactory = new GeometryFactory

  def build(data: OldTileData, tile: Tile): Array[Byte] = {
    val encoder = new VectorTileEncoder(tile.extent, tile.clipBufferSize, false)
    buildNodes(tile, encoder, data.nodes)
    buildRoutes(tile, encoder, data.routes)
    encoder.encode
  }

  private def buildNodes(tile: Tile, encoder: VectorTileEncoder, nodes: Seq[TileDataNode]): Unit = {
    nodes.foreach { node =>
      buildNode(tile, encoder, node)
    }
  }

  private def buildNode(tile: Tile, encoder: VectorTileEncoder, node: TileDataNode): Unit = {
    val coordinate = tile.scale(new Coordinate(node.lon, node.lat))
    val point = geometryFactory.createPoint(coordinate)
    val userData = Seq(
      Some("id" -> node.nodeId.toString),
      node.ref.map(ref => "ref" -> ref),
      node.name.map(name => "name" -> name),
      node.surveyDate.map(surveyDate => "survey" -> surveyDate.yyyymm),
      if (node.proposed) Some("proposed" -> "true") else None
    ).flatten.toMap.asJava
    encoder.addFeature(node.layer, userData, point)
  }

  private def buildRoutes(tile: Tile, encoder: VectorTileEncoder, routes: Seq[TileDataRoute]): Unit = {
    val routesSize = routes.size
    routes.zipWithIndex.foreach { case (tileRoute, index) =>
      if (routesSize > 50) {
        log.info(s"${index + 1}/$routesSize route ${tileRoute.routeName}")
      }
      buildRoute(tile, encoder, tileRoute)
    }
  }

  private def buildRoute(tile: Tile, encoder: VectorTileEncoder, tileRoute: TileDataRoute): Unit = {
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

        val userData = Seq(
          Some("routeId" -> tileRoute.routeId.toString),
          Some("segmentId" -> segment.segmentId.toString),
          Some("segmentElementId" -> segment.segmentElementId.toString),
          Some("pathIds" -> segment.pathIds.mkString(",")),
          Some("name" -> tileRoute.routeName),
          // TODO redesign tiles - Some("oneway" -> segment.oneWay.toString),
          Some("surface" -> segment.surface),
          tileRoute.surveyDate.map(surveyDate => "survey" -> surveyDate.yyyymm),
          tileRoute.state.map(state => "state" -> state),
          tileRoute.scopes.map(value => "scope" -> value)
        ).flatten.toMap.asJava

        encoder.addFeature(tileRoute.layer, userData, simplifiedLineString)
      }
    }
  }
}
