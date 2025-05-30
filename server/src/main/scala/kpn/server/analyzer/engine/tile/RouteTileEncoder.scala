package kpn.server.analyzer.engine.tile

import kpn.api.common.RouteType
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileSegment
import kpn.server.analyzer.engine.tiles.TileData
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilder
import kpn.server.analyzer.engine.tiles.TileFileRepository
import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.json.Json
import no.ecc.vectortile.VectorTileEncoder
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Component

import scala.jdk.CollectionConverters.MapHasAsJava

@Component
class RouteTileEncoder(
  vectorTileRepository: TileFileRepository,
  tileDataNodeBuilder: TileDataNodeBuilder
) {
  private val geometryFactory = new GeometryFactory

  def encode(tileData: TileData): Unit = {
    val tileBytes = encodeTile(tileData)
    if (tileBytes.nonEmpty) {
      vectorTileRepository.saveOrUpdate(tileData.routeType.entryName, tileData.tile, tileBytes)
    }
  }

  private def encodeTile(tileData: TileData): Array[Byte] = {
    val encoder = new VectorTileEncoder(tileData.tile.extent, tileData.tile.clipBufferSize, false)
    encodeTileNodes(tileData.routeType, tileData.nodes, tileData.tile, encoder)
    encodeTileRoutes(tileData.routes, encoder)
    encoder.encode()
  }

  private def encodeTileRoutes(routeTileInfos: Seq[RouteTileDoc], encoder: VectorTileEncoder): Unit = {
    routeTileInfos.foreach { routeTileDoc =>
      encodeTileRoute(encoder, routeTileDoc)
    }
  }

  private def encodeTileNodes(routeType: RouteType, nodeTileInfos: Seq[NodeTileInfo], tile: Tile, encoder: VectorTileEncoder): Unit = {
    nodeTileInfos.foreach { nodeTileInfo =>
      tileDataNodeBuilder.build(routeType, nodeTileInfo).foreach { tileDataNode =>
        encodeTileNode(encoder, tile, tileDataNode)
      }
    }
  }

  private def encodeTileNode(encoder: VectorTileEncoder, tile: Tile, tileDataNode: TileDataNode): Unit = {
    val point = buildNodePoint(tile, tileDataNode)
    val userData = buildNodeUserData(tileDataNode)
    encoder.addFeature(tileDataNode.layer, userData, point)
  }

  private def buildNodePoint(tile: Tile, tileDataNode: TileDataNode): Point = {
    val worldCoordinate = new Coordinate(lonToWorldX(tileDataNode.lon), latToWorldY(tileDataNode.lat))
    val coordinate = tile.scale(worldCoordinate)
    geometryFactory.createPoint(coordinate)
  }

  private def buildNodeUserData(tileDataNode: TileDataNode): java.util.Map[String, String] = {
    Seq(
      Some("id" -> tileDataNode.nodeId.toString),
      tileDataNode.ref.map(ref => "ref" -> ref),
      tileDataNode.name.map(name => "name" -> name),
      tileDataNode.surveyDate.map(surveyDate => "survey" -> surveyDate.yyyymm),
      if (tileDataNode.proposed) Some("proposed" -> "true") else None
    ).flatten.toMap.asJava
  }

  private def encodeTileRoute(encoder: VectorTileEncoder, routeTileDoc: RouteTileDoc): Unit = {
    routeTileDoc.segments.foreach { segment =>
      val userData = buildRouteUserData(routeTileDoc, segment)
      segment.geometries.foreach { geometryString =>
        val lineString = buildRouteLineString(geometryString)
        encoder.addFeature(routeTileDoc.layer, userData, lineString)
      }
    }
  }

  private def buildRouteLineString(geometryString: String): LineString = {
    val coordinates: Array[Coordinate] = Json.value(geometryString, classOf[CoordinateArray]).coordinates
    val flipped = coordinates.map(c => new Coordinate(c.y, c.x))
    geometryFactory.createLineString(flipped)
  }

  private def buildRouteUserData(routeTileDoc: RouteTileDoc, segment: RouteTileSegment): java.util.Map[String, String] = {
    Seq(
      Some("routeId" -> routeTileDoc.routeId.toString),
      Some("name" -> routeTileDoc.routeName),
      segment.segmentId.map(segmentId => "segmentId" -> segmentId.toString),
      segment.segmentElementId.map(segmentElementId => "segmentElementId" -> segmentElementId.toString),
      routeTileDoc.scope.map(scope => "scope" -> scope.entryName),
      routeTileDoc.survey.map(survey => "survey" -> survey),
      routeTileDoc.error.map(error => "error" -> error)
    ).flatten.toMap.asJava
  }
}
