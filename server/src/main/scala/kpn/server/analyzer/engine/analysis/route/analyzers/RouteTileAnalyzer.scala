package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.Relation
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileData
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileSegment
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.wayToWorldCoordinates
import kpn.server.analyzer.engine.tiles.domain.Tile
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineSegment
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.TopologyException
import org.locationtech.jts.io.ParseException
import org.locationtech.jts.io.WKTReader
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier
import org.springframework.stereotype.Component

case class TileSegment(
  segmentId: Long,
  segmentElementId: Long,
  worldCoordinates: Seq[Coordinate]
)

@Component
class RouteTileAnalyzer(lineSegmentTileCalculator: LineSegmentTileCalculator) extends RouteAnalyzer {
  private val geometryFactory = new GeometryFactory

  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    val tileSegments = context.segments.flatMap { segment =>
      segment.elements.map { element =>
        val worldCoordinates = element.nodes.map(node => new Coordinate(lonToWorldX(node.lon), latToWorldY(node.lat)))
        TileSegment(segment.id, element.id, worldCoordinates)
      }
    }

    val tiles = determineTiles(context.relation)

    val zoomLevels = ZoomLevel.newMinZoom.to(ZoomLevel.newMaxZoom).toSeq
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

  private def clipGeometry(tile: Tile, geometry: Geometry): Geometry = {
    try {
      var clippedGeometry = tile.tileEnvelope.intersection(geometry)
      // some times a intersection is returned as an empty geometry.
      // going via wkt fixes the problem.
      if (clippedGeometry.isEmpty && geometry.intersects(tile.tileEnvelope)) {
        val originalViaWkt = new WKTReader().read(geometry.toText)
        clippedGeometry = tile.tileEnvelope.intersection(originalViaWkt)
      }
      clippedGeometry
    } catch {
      case e: TopologyException =>
        // could not intersect. original geometry will be used instead
        geometry
      case e1: ParseException =>
        // could not encode/decode WKT. original geometry will be used instead
        geometry
    }
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
    val error = if (context.facts.exists(_.isError)) Some("true") else None

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
    val scaledCoordinates = tileSegment.worldCoordinates.map(tile.scale)
    val lineString = geometryFactory.createLineString(scaledCoordinates.toArray)
    val simplifiedLineString = if (!tile.detailed) {
      DouglasPeuckerSimplifier.simplify(lineString, 1).asInstanceOf[LineString]
    }
    else {
      lineString
    }

    if (simplifiedLineString.getLength < 1.0d) {
      None
    }
    else {
      val clippedGeometry = clipGeometry(tile, simplifiedLineString)

      // ignore geometry if empty after clipping
      if (clippedGeometry.isEmpty) {
        None
      }
      else {
        if (clippedGeometry.getLength < 1.0d) {
          None
        }
        else {
          val geometryString = clippedGeometry.getCoordinates().map(coordinate => s"[${Math.floor(coordinate.x).toInt},${Math.floor(coordinate.y).toInt}]").mkString("[", ",", "]")
          Some(geometryString)
        }
      }
    }
  }
}
