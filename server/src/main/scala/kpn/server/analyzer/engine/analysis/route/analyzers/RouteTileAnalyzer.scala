package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.Relation
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileData
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tiles.domain.ClipBuffer
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.wayToWorldCoordinates
import kpn.server.analyzer.engine.tiles.domain.Tile
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineSegment
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.Polygon
import org.locationtech.jts.geom.TopologyException
import org.locationtech.jts.io.ParseException
import org.locationtech.jts.io.WKTReader
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier
import org.springframework.stereotype.Component

case class TileSegment(
  worldCoordinates: Seq[Coordinate]
)

@Component
class RouteTileAnalyzer(lineSegmentTileCalculator: LineSegmentTileCalculator) extends RouteAnalyzer {
  private val geometryFactory = new GeometryFactory
  private val extent = Tile.EXTENT
  private val clipBuffer: ClipBuffer = Tile.CLIP_BUFFER
  private val tileEnvelope: Polygon = {
    val size = extent.toDouble
    val coords = new Array[Coordinate](5)
    coords(0) = new Coordinate(0d - clipBuffer.left, size + clipBuffer.bottom)
    coords(1) = new Coordinate(size + clipBuffer.right, size + clipBuffer.bottom)
    coords(2) = new Coordinate(size + clipBuffer.right, 0d - clipBuffer.top)
    coords(3) = new Coordinate(0d - clipBuffer.left, 0d - clipBuffer.top)
    coords(4) = coords(0)
    new GeometryFactory().createPolygon(coords)
  }

  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    val tileSegments = context.segments.flatMap { segment =>
      segment.elements.map { element =>
        val worldCoordinates = element.nodes.map(node => new Coordinate(lonToWorldX(node.lon), latToWorldY(node.lat)))
        TileSegment(worldCoordinates)
      }
    }

    val tiles = determineTiles(context.relation)

    val tileDatas = if (context.nodeNetwork) {
      Seq.empty
    }
    else {
      val zoomLevels = ZoomLevel.newMinZoom.to(ZoomLevel.maxZoom).toSeq
      val datas = zoomLevels.flatMap { zoomLevel =>
        if (includeZoomLevel(context, zoomLevel)) {
          buildTileRouteData(context, zoomLevel, tiles, tileSegments)
        }
        else {
          Seq.empty
        }
      }
      datas
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
      (ZoomLevel.newMinZoom to ZoomLevel.maxZoom).flatMap { z =>
        lineSegmentTileCalculator.tiles(z, lineSegments)
      }
    }.distinct.sortBy(tile => (tile.z, tile.x, tile.y))
  }

  private def clipGeometry(z: Int, geometry: Geometry): Geometry = {
    try {
      var clippedGeometry = tileEnvelope.intersection(geometry)
      // some times a intersection is returned as an empty geometry.
      // going via wkt fixes the problem.
      if (clippedGeometry.isEmpty && geometry.intersects(tileEnvelope)) {
        val originalViaWkt = new WKTReader().read(geometry.toText)
        clippedGeometry = tileEnvelope.intersection(originalViaWkt)
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

  private def includeZoomLevel(context: RouteDetailAnalysisContext, zoomLevel: Int): Boolean = {
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

  private def buildTileRouteData(context: RouteDetailAnalysisContext, zoomLevel: Int, tiles: Seq[Tile], tileSegments: Seq[TileSegment]): Seq[RouteTileData] = {
    val zoomLevelTiles = tiles.filter(_.z == zoomLevel)
    zoomLevelTiles.flatMap { tile =>
      val geometries: Seq[String] = tileSegments.flatMap { tileSegment =>
        tileSegmentToGeometry(tile, tileSegment)
      }
      if (geometries.isEmpty) {
        None
      }
      else {
        Some(
          RouteTileData(
            tile.z,
            tile.x,
            tile.y,
            context.scopes.head,
            geometries
          )
        )
      }
    }
  }

  private def tileSegmentToGeometry(tile: Tile, tileSegment: TileSegment): Option[String] = {
    // TODO redesign tiles - need to include bounding box check? // if (tile.contains(worldCoordinates))
    val scaledCoordinates = tileSegment.worldCoordinates.map(tile.scale)
    val lineString = geometryFactory.createLineString(scaledCoordinates.toArray)
    val simplifiedLineString = if (tile.z < 14) {
      DouglasPeuckerSimplifier.simplify(lineString, 14).asInstanceOf[LineString]
    }
    else {
      lineString
    }

    if (simplifiedLineString.getLength < 1.0d) {
      None
    }
    else {
      val clippedGeometry = clipGeometry(tile.z, simplifiedLineString)

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
