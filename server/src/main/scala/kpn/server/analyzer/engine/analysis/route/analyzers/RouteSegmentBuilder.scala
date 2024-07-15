package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.tiles.ZoomLevel
import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latLonCoordinatesToWorldCoordinates
import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.OldTile
import kpn.server.analyzer.engine.tiles.domain.Point
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.domain.RouteTileSegment
import kpn.server.json.Json
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineSegment
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier

class RouteSegmentBuilder(zoomLevel: Int) {

  private val distanceTolerance = {
    val tileX = (zoomLevel - 7) * 65
    (OldTile.lon(zoomLevel, tileX + 1) - OldTile.lon(zoomLevel, tileX)) / 256d
  }

  private val geometryFactory = new GeometryFactory

  def from(tileInfo: RouteTileInfo): Seq[RouteTileSegment] = {
    tileInfo.segments.flatMap { segment =>
      tileInfo.segmentElements.map { segmentElement =>
        val coordinates = Json.value(segmentElement.coordinates, classOf[CoordinateArray]).coordinates.toSeq
        val worldCoordinates = latLonCoordinatesToWorldCoordinates(coordinates)
        val lineSegments = worldCoordinates.sliding(2).map { case Seq(c1, c2) =>
          new LineSegment(c1, c2)
        }.toSeq
        val pathIds = tileInfo.paths.filter(_.elementIds.contains(segmentElement.segmentElementId)).map(_.id)
        RouteTileSegment(
          segment.id,
          segmentElement.segmentElementId,
          pathIds = pathIds,
          oneWay = false, // TODO redesign - still needed???
          segmentElement.surface,
          lineSegments
        )
      }
    }
  }

  private def toLines(coordinates: Array[Coordinate]): Seq[Line] = {
    if (coordinates.size < 2) {
      Seq.empty
    }
    else {
      if (zoomLevel < ZoomLevel.vectorTileMinZoom) {
        val lineString = geometryFactory.createLineString(coordinates)
        val simplifiedLineString: LineString = DouglasPeuckerSimplifier.simplify(lineString, distanceTolerance).asInstanceOf[LineString]
        val simplifiedCoordinates = simplifiedLineString.getCoordinates.toSeq
        simplifiedCoordinates.sliding(2).flatMap { case Seq(c1, c2) =>
          val line = Line(Point(c1.x, c1.y), Point(c2.x, c2.y))
          // TODO MAP should check if line is within clipbounds of tile - return none if outside clipBounds
          if (line.length > 0.00000001) Some(line) else None
        }.toSeq
      }
      else {
        coordinates.toSeq.sliding(2).flatMap { case Seq(c1, c2) =>
          // TODO MAP should make sure that empty lines are eliminated long before this point !!!
          val line = Line(Point(c1.x, c1.y), Point(c2.x, c2.y))
          if (line.length > 0.00000001) Some(line) else None
        }.toSeq
      }
    }
  }
}
