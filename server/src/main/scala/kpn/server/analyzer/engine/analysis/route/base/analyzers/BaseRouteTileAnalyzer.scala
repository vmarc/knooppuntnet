package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.FeatureLayer
import kpn.api.common.RouteScope
import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.Relation
import kpn.core.analysis.Facts
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileData
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileSegment
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.wayToWorldCoordinates
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileContext
import kpn.server.analyzer.engine.tiles.domain.TileCoordinate
import kpn.server.analyzer.engine.tiles.domain.TileUtil
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineSegment
import org.springframework.stereotype.Component

case class TileSegment(
  segmentId: Long,
  segmentElementId: Long,
  worldCoordinates: Seq[Coordinate]
)

@Component
class BaseRouteTileAnalyzer(lineSegmentTileCalculator: LineSegmentTileCalculator) extends BaseRouteAnalyzer {
  private val geometryFactory = new GeometryFactory

  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    val tileSegments = context.analysisSegments.flatMap { segment =>
      segment.elements.map { element =>
        val worldCoordinates = element.nodes.map(node => new Coordinate(lonToWorldX(node.lon), latToWorldY(node.lat)))
        TileSegment(segment.id, element.id, worldCoordinates)
      }
    }

    val tiles = determineTiles(context.relation)

    val zoomLevels = ZoomLevel.newMinZoom.to(ZoomLevel.newMaxZoom)
    val tileDatas = zoomLevels.flatMap { zoomLevel =>
      if (includeRoute(context, zoomLevel)) {
        buildTileRouteData(context, zoomLevel, tiles, tileSegments)
      }
      else {
        Seq.empty
      }
    }

    context.copy(
      tiles = tiles.map(_.name),
      _tileDatas = Some(tileDatas)
    )
  }

  private def determineTiles(relation: Relation): Seq[Tile] = {
    relation.wayMembers.map(_.way).flatMap { way =>
      if (way.nodes.sizeIs > 1) {
        val worldCoordinates = wayToWorldCoordinates(way)
        val lineSegments = worldCoordinates.sliding(2).map { case Seq(c1, c2) =>
          new LineSegment(c1, c2)
        }.toSeq
        (ZoomLevel.newMinZoom to ZoomLevel.newMaxZoom).flatMap { z =>
          lineSegmentTileCalculator.tiles(z, lineSegments)
        }
      }
      else {
        Seq.empty
      }
    }.distinct.sortBy(tile => (tile.z, tile.x, tile.y))
  }

  private def includeRoute(context: BaseRouteAnalysisContext, zoomLevel: Int): Boolean = {
    if (context.nodeNetwork && zoomLevel >= ZoomLevel.minZoomNodeNetwork) {
      return true
    }
    val includedScopes = if (zoomLevel < ZoomLevel.minZoomNational) {
      Seq(RouteScope.international)
    }
    else if (zoomLevel < ZoomLevel.minZoomRegional) {
      Seq(RouteScope.international, RouteScope.national)
    }
    else if (zoomLevel < ZoomLevel.minZoomLocal) {
      Seq(RouteScope.international, RouteScope.national, RouteScope.regional)
    }
    else {
      Seq(RouteScope.international, RouteScope.national, RouteScope.regional, RouteScope.local, RouteScope.unknown)
    }
    includedScopes.exists(context.scopes.contains)
  }

  private def buildTileRouteData(
    context: BaseRouteAnalysisContext,
    zoomLevel: Int,
    tiles: Seq[Tile],
    tileSegments: Seq[TileSegment]
  ): Seq[RouteTileData] = {

    val tileContext = TileContext.route(zoomLevel)
    val layer = if (context.nodeNetwork) FeatureLayer.nodeRoute else FeatureLayer.route
    val scope = if (context.nodeNetwork) {
      None
    } else {
      context.scopes.headOption
    }
    val survey = context.lastSurvey.map(_.yyyymm)
    val error = if (context.facts.exists(Facts.isError)) Some("true") else None

    val zoomLevelTiles = tiles.filter(_.z == zoomLevel)
    zoomLevelTiles.flatMap { tile =>
      val segments = tileSegments.flatMap { tileSegment =>
        tileSegmentToGeometry(tileContext, tile, tileSegment).flatMap { geometry =>
          val segmentId = Option.when(tile.z > 6) {
            tileSegment.segmentId
          }
          val segmentElementId = Option.when(tileContext.detailed) {
            tileSegment.segmentElementId
          }
          Some(
            RouteTileSegment(
              segmentId,
              segmentElementId,
              Seq(geometry)
            )
          )
        }
      }
      if (segments.isEmpty) {
        None
      }
      else {
        Some(
          RouteTileData(
            tile.z,
            tile.x,
            tile.y,
            layer,
            scope,
            survey,
            error,
            context.proposed,
            segments
          )
        )
      }
    }
  }

  private def tileSegmentToGeometry(tileContext: TileContext, tile: Tile, tileSegment: TileSegment): Option[String] = {
    val tileCoordinates = TileUtil.tileCoordinates(tileContext, tile, tileSegment.worldCoordinates)

    // TODO redesign-tile -  the longEnough logic is already covered in TileUtil.tileCoordinates ??? or it could be???
    if (longEnough(tileCoordinates)) {
      val geometryString = tileCoordinates.map(coordinate => s"[${coordinate.x},${coordinate.y}]").mkString("[", ",", "]")
      Some(geometryString)
    }
    else {
      None
    }
  }

  private def longEnough(tileCoordinates: Seq[TileCoordinate]): Boolean = {
    if (tileCoordinates.isEmpty) {
      false
    }
    else {
      val flipped: Array[Coordinate] = tileCoordinates.toArray.map(c => new Coordinate(c.y, c.x))
      val lineString = geometryFactory.createLineString(flipped)
      lineString.getLength > 1.5
    }
  }
}
