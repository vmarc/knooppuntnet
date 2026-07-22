package kpn.server.analyzer.engine.tile

import kpn.api.common.FeatureLayer
import kpn.api.common.RouteType
import kpn.core.util.CoordinateUtil
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileInfo
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileSegment
import kpn.server.analyzer.engine.tiles.TileData
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilder
import kpn.server.analyzer.engine.tiles.TileFileRepository
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import kpn.server.analyzer.engine.tiles.domain.RouteTiles
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.Point
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class RouteTileEncoder(
  vectorTileRepository: TileFileRepository,
  tileDataNodeBuilder: TileDataNodeBuilder
) {
  private val geometryFactory = new GeometryFactory
  private val emptyUserData = new java.util.HashMap[String, String]()

  def encode(tileData: TileData): Unit = {
    val tileBytes = encodeTile(tileData)
    if (tileBytes.nonEmpty) {
      vectorTileRepository.saveOrUpdate(tileData.routeType.entryName, tileData.tile, tileBytes)
    }
  }

  private def encodeTile(tileData: TileData): Array[Byte] = {
    val nodeFeatures = buildNodeFeatures(tileData.routeType, tileData.nodeTileInfos, tileData.tile)
    val routeFeatures = buildRouteFeatures(tileData.tile.z, tileData.routeTileInfos)
    val features = nodeFeatures ++ routeFeatures
    TileEncoder.encode(tileData.tile.z, features)
  }

  private def buildNodeFeatures(routeType: RouteType, nodeTileInfos: Seq[NodeTileInfo], tile: Tile): Seq[Feature] = {
    nodeTileInfos.flatMap { nodeTileInfo =>
      tileDataNodeBuilder.build(routeType, nodeTileInfo).map { tileDataNode =>
        nodeFeature(tile, tileDataNode)
      }
    }
  }

  private def buildRouteFeatures(zoomLevel: Int, routeTileInfos: Seq[RouteTileInfo]): Seq[Feature] = {
    routeTileInfos.flatMap { routeTileInfo =>
      buildRouteSegmentsFeatures(zoomLevel, routeTileInfo)
    }
  }

  private def nodeFeature(tile: Tile, tileDataNode: TileDataNode): Feature = {
    val point = buildNodePoint(tile, tileDataNode)
    val userData = buildNodeUserData(tileDataNode)
    Feature(tileDataNode.layer, userData, point)
  }

  private def buildNodePoint(tile: Tile, tileDataNode: TileDataNode): Point = {
    val worldCoordinate = new Coordinate(lonToWorldX(tileDataNode.lon), latToWorldY(tileDataNode.lat))
    val coordinate = RouteTiles.toTileCoordinate(tile, worldCoordinate)
    geometryFactory.createPoint(coordinate)
  }

  private def buildNodeUserData(tileDataNode: TileDataNode): Map[String, String] = {
    Seq(
      Some("id" -> tileDataNode.nodeId.toString),
      tileDataNode.ref.map(ref => "ref" -> ref),
      tileDataNode.name.map(name => "name" -> name),
      tileDataNode.surveyDate.map(surveyDate => "survey" -> surveyDate.yyyymm),
      if (tileDataNode.proposed) Some("proposed" -> "true") else None
    ).flatten.toMap
  }

  private def buildRouteSegmentsFeatures(zoomLevel: Int, routeTileInfo: RouteTileInfo): Seq[Feature] = {
    routeTileInfo.segments.flatMap { segment =>
      val lineStrings = segment.lines.map(buildRouteLineString).filter(_.getLength > 0.5)
      if (lineStrings.nonEmpty) {
        val userData = buildRouteUserData(zoomLevel, routeTileInfo, segment)
        lineStrings.map { lineString =>
          Feature(routeTileInfo.layer, userData, lineString)
        }
      }
      else {
        Seq.empty
      }
    }
  }

  private def buildRouteLineString(line: String): LineString = {
    val coordinates = CoordinateUtil.stringToCoordinates(line)
    geometryFactory.createLineString(coordinates)
  }

  private def buildRouteUserData(zoomLevel: Int, routeTileInfo: RouteTileInfo, segment: RouteTileSegment): Map[String, String] = {
    if (routeTileInfo.layer == FeatureLayer.nodeRoute && zoomLevel < ZoomLevel.minZoomNodeNetworkUserData) {
      Seq(
        Some("routeId" -> routeTileInfo.routeId.toString),
        routeTileInfo.survey.map(survey => "survey" -> survey),
        routeTileInfo.error.map(error => "error" -> error)
      ).flatten.toMap
    }
    else {
      Seq(
        Some("routeId" -> routeTileInfo.routeId.toString),
        Some("name" -> routeTileInfo.routeName),
        segment.segmentId.map(segmentId => "segmentId" -> segmentId.toString),
        segment.segmentElementId.map(segmentElementId => "segmentElementId" -> segmentElementId.toString),
        routeTileInfo.scope.map(scope => "scope" -> scope.entryName),
        routeTileInfo.survey.map(survey => "survey" -> survey),
        routeTileInfo.error.map(error => "error" -> error)
      ).flatten.toMap
    }
  }
}
