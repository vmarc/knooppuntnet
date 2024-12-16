package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.Relation
import kpn.core.analysis.Facts
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileData
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileSegment
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.wayToWorldCoordinates
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileUtil
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.LineSegment
import org.springframework.stereotype.Component

case class TileSegment(
  segmentId: Long,
  segmentElementId: Long,
  worldCoordinates: Seq[Coordinate]
)

@Component
class RouteTileAnalyzer(lineSegmentTileCalculator: LineSegmentTileCalculator) extends RouteDetailAnalyzer {

  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
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
      val worldCoordinates = wayToWorldCoordinates(way)
      val lineSegments = worldCoordinates.sliding(2).map { case Seq(c1, c2) =>
        new LineSegment(c1, c2)
      }.toSeq
      (ZoomLevel.newMinZoom to ZoomLevel.newMaxZoom).flatMap { z =>
        lineSegmentTileCalculator.tiles(z, lineSegments)
      }
    }.distinct.sortBy(tile => (tile.z, tile.x, tile.y))
  }

  private def includeRoute(context: RouteDetailAnalysisContext, zoomLevel: Int): Boolean = {
    if (context.nodeNetwork && zoomLevel >= 6) {
      return true
    }
    val includedScopes = if (zoomLevel < 7) {
      Seq("international")
    }
    else if (zoomLevel < 9) {
      Seq("international", "national")
    }
    else if (zoomLevel < 11) {
      Seq("international", "national", "regional")
    }
    else {
      Seq("international", "national", "regional", "local", "unkown")
    }
    includedScopes.exists(context.scopes.contains)
  }

  private def buildTileRouteData(
    context: RouteDetailAnalysisContext,
    zoomLevel: Int,
    tiles: Seq[Tile],
    tileSegments: Seq[TileSegment]
  ): Seq[RouteTileData] = {

    val layer = if (context.nodeNetwork) "node-route" else "route"
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
        tileSegmentToGeometry(tile, tileSegment).flatMap { geometry =>
          val segmentId = if (tile.z > 6) {
            Some(tileSegment.segmentId)
          }
          else {
            None
          }
          val segmentElementId = if (tile.detailed) {
            Some(tileSegment.segmentElementId)
          }
          else {
            None
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
            segments
          )
        )
      }
    }
  }

  private def tileSegmentToGeometry(tile: Tile, tileSegment: TileSegment): Option[String] = {
    val tileCoordinates = TileUtil.tileCoordinates(tile, tileSegment.worldCoordinates)
    if (tileCoordinates.isEmpty) {
      None
    }
    else {
      val geometryString = tileCoordinates.map(coordinate => s"[${coordinate.x},${coordinate.y}]").mkString("[", ",", "]")
      Some(geometryString)
    }
  }
}
