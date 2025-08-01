package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.FeatureLayer
import kpn.api.common.RouteScope
import kpn.api.common.data.Way
import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.Relation
import kpn.core.analysis.Facts
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileData
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileSegment
import kpn.server.analyzer.engine.monitor.state.MonitorStateTileCoordinateSimplifier.simplify
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.wayToWorldCoordinates
import kpn.server.analyzer.engine.tiles.domain.RouteTiles
import kpn.server.analyzer.engine.tiles.domain.Tile
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
    val tileSegments = buildTileSegments(context)
    val tiles = determineTiles(context.relation)
    val tileDatas = buildTileDatas(context, tileSegments, tiles)
    context.copy(
      tiles = tiles.map(_.name),
      _tileDatas = Some(tileDatas)
    )
  }

  private def buildTileDatas(
    context: BaseRouteAnalysisContext,
    tileSegments: Seq[TileSegment],
    tiles: Seq[Tile]
  ): Seq[RouteTileData] = {
    val zoomLevels = ZoomLevel.newMinZoom.to(ZoomLevel.newMaxZoom)
    zoomLevels.flatMap { zoomLevel =>
      if (shouldIncludeRouteForZoomLevel(context, zoomLevel)) {
        buildTileRouteData(context, zoomLevel, tiles, tileSegments)
      }
      else {
        Seq.empty
      }
    }
  }

  private def buildTileSegments(context: BaseRouteAnalysisContext): Seq[TileSegment] = {
    context.analysisSegments.flatMap { segment =>
      segment.elements.map { element =>
        val worldCoordinates = element.nodes.map { node =>
          new Coordinate(lonToWorldX(node.lon), latToWorldY(node.lat))
        }
        TileSegment(segment.id, element.id, worldCoordinates)
      }
    }
  }

  private def determineTiles(relation: Relation): Seq[Tile] = {
    relation.wayMembers.map(_.way)
      .flatMap(tilesForWay)
      .distinct.
      sortBy(_.name)
  }

  private def tilesForWay(way: Way): Seq[Tile] = {
    if (way.nodes.sizeIs > 1) {
      val worldCoordinates = wayToWorldCoordinates(way)
      val lineSegments = worldCoordinates
        .sliding(2)
        .map { case Seq(c1, c2) => new LineSegment(c1, c2) }
        .toSeq

      (ZoomLevel.newMinZoom to ZoomLevel.newMaxZoom).flatMap { z =>
        lineSegmentTileCalculator.tiles(z, lineSegments)
      }
    }
    else {
      Seq.empty
    }
  }

  private def shouldIncludeRouteForZoomLevel(context: BaseRouteAnalysisContext, zoomLevel: Int): Boolean = {
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

    val layer = if (context.nodeNetwork) FeatureLayer.nodeRoute else FeatureLayer.route
    val scope = if (context.nodeNetwork) None else context.scopes.headOption
    val survey = context.lastSurvey.map(_.yyyymm)
    val error = if (context.facts.exists(Facts.isError)) Some("true") else None

    tiles.filter(_.z == zoomLevel).flatMap { tile =>
      val segments = buildSegmentsForTile(tile, tileSegments)
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

  private def buildSegmentsForTile(tile: Tile, tileSegments: Seq[TileSegment]): Seq[RouteTileSegment] = {

    // TODO redesign - further refactor and test

    if (RouteTiles.detailed(tile.z)) {
      tileSegments.flatMap { tileSegment =>
        tileSegmentToGeometry(tile, tileSegment).map { geometry =>
          RouteTileSegment(
            Some(tileSegment.segmentId),
            Some(tileSegment.segmentElementId),
            Seq(geometry)
          )
        }
      }
    }
    else {
      val segmentIds = tileSegments.map(_.segmentId).distinct.sorted
      segmentIds.flatMap { segmentId =>
        val segments = tileSegments.filter(_.segmentId == segmentId)
        val tileCoordinateSeqs = segments.map(_.worldCoordinates).map(wc => TileUtil.routeTileCoordinates(tile, wc)).filter(_.nonEmpty)
        if (tileCoordinateSeqs.nonEmpty) {
          val simplified = simplify(tileCoordinateSeqs)
          val lines = simplified.map(tileCoordinates => tileCoordinates.map(coordinate => s"[${coordinate.x},${coordinate.y}]").mkString("[", ",", "]"))
          Some(
            RouteTileSegment(
              Some(segmentId),
              None,
              lines
            )
          )
        }
        else {
          None
        }
      }
    }
  }

  private def tileSegmentToGeometry(tile: Tile, tileSegment: TileSegment): Option[String] = {
    val tileCoordinates = TileUtil.routeTileCoordinates(tile, tileSegment.worldCoordinates)
    if (tileCoordinates.nonEmpty) {
      Some(tileCoordinates
        .map(coordinate => s"[${coordinate.x},${coordinate.y}]")
        .mkString("[", ",", "]")
      )
    }
    else {
      None
    }
  }
}
