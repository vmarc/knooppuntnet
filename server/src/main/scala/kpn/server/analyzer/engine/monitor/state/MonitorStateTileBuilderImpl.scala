package kpn.server.analyzer.engine.monitor.state

import kpn.api.base.ObjectId
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileCoordinate
import kpn.server.analyzer.engine.tiles.domain.TileUtil
import kpn.server.monitor.domain.MonitorSegment
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.domain.MonitorStateTile
import kpn.server.monitor.domain.MonitorStateTileDeviation
import kpn.server.monitor.route.update.MonitorStateDeviationWorldCoordinates
import org.locationtech.jts.geom.Coordinate
import org.springframework.stereotype.Component

case class SegmentWorldCoordinates(
  relationId: Long,
  segmentId: Long,
  coordinates: Seq[Coordinate],
  xmin: Double,
  xmax: Double,
  ymin: Double,
  ymax: Double
)

@Component
class MonitorStateTileBuilderImpl(
  lineSegmentTileCalculator: LineSegmentTileCalculator
) extends MonitorStateTileBuilder {

  def build(state: MonitorState): Seq[MonitorStateTile] = {
    val worldCoordinateMatchesLines = buildWorldCoordinateMatchesLines(state)
    val worldCoordinateSegments = state.segments.map { segment =>
      val coordinates = CoordinateTransform.lineToWorldCoordinates(segment.coordinates)
      val xmin = coordinates.minBy(_.x).x
      val xmax = coordinates.maxBy(_.x).x
      val ymin = coordinates.minBy(_.y).y
      val ymax = coordinates.maxBy(_.y).y
      SegmentWorldCoordinates(
        segment.relationId,
        segment.segmentId,
        coordinates,
        xmin,
        xmax,
        ymin,
        ymax
      )
    }
    val deviations = buildDeviations(state)
    val allWorldCoordinateReferenceLines = worldCoordinateMatchesLines ++ worldCoordinateSegments.map(_.coordinates) ++ deviations.flatMap(_.worldCoordinateLines)
    val tiles = lineSegmentTileCalculator.tilesForLines(allWorldCoordinateReferenceLines)
    tiles.flatMap { tile =>
      val matchesLines = {
        val tileCoordinateSeqs = worldCoordinateMatchesLines.map(coordinates => TileUtil.routeTileCoordinates(tile, coordinates)).filter(_.nonEmpty)
        val simplified = simplifyCoordinateSeqs(tileCoordinateSeqs)
        simplified.map(tileCoordinates => tileCoordinates.map(coordinate => s"[${coordinate.x},${coordinate.y}]").mkString("[", ",", "]"))
      }
      val tileDeviations = buildTileDeviations(tile, deviations)
      val segmentIds = worldCoordinateSegments.map(_.segmentId).distinct.sorted
      val segments = segmentIds.flatMap { segmentId =>
        val segs = worldCoordinateSegments.filter(_.segmentId == segmentId)
        val tileCoordinateSeqs = segs.map(segment => TileUtil.routeTileCoordinates(tile, segment.coordinates)).filter(_.nonEmpty)
        val simplified = simplifyCoordinateSeqs(tileCoordinateSeqs)
        val coordinateStrings = simplified.map(tileCoordinates => tileCoordinates.map(coordinate => s"[${coordinate.x},${coordinate.y}]").mkString("[", ",", "]"))
        coordinateStrings.map { coordinateStrings =>
          MonitorSegment(
            relationId = segs.head.relationId,
            segmentId = segmentId,
            coordinates = coordinateStrings
          )
        }
      }

      if (matchesLines.isEmpty && tileDeviations.isEmpty && segments.isEmpty) {
        None
      }
      else {
        Some(
          MonitorStateTile(
            ObjectId(),
            state.routeId,
            state.relationId,
            tile.z,
            tile.x,
            tile.y,
            tileDeviations,
            matchesLines,
            segments
          )
        )
      }
    }
  }

  private def simplifyCoordinateSeqs(coordinateSeqs: Seq[Seq[TileCoordinate]]): Seq[Seq[TileCoordinate]] = {
    MonitorStateTileCoordinateSimplifier.simplify(coordinateSeqs)
  }

  private def buildTileDeviations(tile: Tile, deviations: Seq[MonitorStateDeviationWorldCoordinates]): Seq[MonitorStateTileDeviation] = {
    deviations.flatMap { deviation =>
      val lines = deviation.worldCoordinateLines.flatMap(worldCoordinates => TileUtil.toTileLine(tile, worldCoordinates))
      if (lines.isEmpty) {
        None
      }
      else {
        Some(
          MonitorStateTileDeviation(
            deviation.id,
            lines
          )
        )
      }
    }
  }

  private def buildWorldCoordinateMatchesLines(state: MonitorState): Seq[Seq[Coordinate]] = {
    state.matchesLines.map(CoordinateTransform.lineToWorldCoordinates)
  }

  private def buildDeviations(state: MonitorState): Seq[MonitorStateDeviationWorldCoordinates] = {
    state.deviations.map { deviation =>
      val worldCoordinateLines = deviation.lines.map(CoordinateTransform.lineToWorldCoordinates)
      MonitorStateDeviationWorldCoordinates(
        deviation.id,
        worldCoordinateLines
      )
    }
  }
}
