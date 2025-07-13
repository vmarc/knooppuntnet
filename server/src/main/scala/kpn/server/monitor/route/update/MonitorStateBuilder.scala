package kpn.server.monitor.route.update

import kpn.api.common.tiles.ZoomLevel
import kpn.core.util.Log
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileUtil
import kpn.server.json.Json
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.domain.MonitorStateTile
import kpn.server.monitor.domain.MonitorStateTileDeviation
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.LineSegment
import org.springframework.stereotype.Component

@Component
class MonitorStateBuilder(
  lineSegmentTileCalculator: LineSegmentTileCalculator
) {

  private val log = Log(classOf[MonitorStateBuilder])

  def build(state: MonitorState): MonitorState = {
    val stateTiles = buildStateTiles(state)
    state.copy(tiles = stateTiles)
  }

  private def buildStateTiles(state: MonitorState): Seq[MonitorStateTile] = {
    val worldCoordinateMatchesLines = buildWorldCoordinateMatchesLines(state)
    val deviations = buildDeviations(state)
    val allWorldCoordinateReferenceLines = worldCoordinateMatchesLines ++ deviations.flatMap(_.worldCoordinateLines)
    val tiles = tilesForReference(allWorldCoordinateReferenceLines)
    val stateTiles = tiles.flatMap { tile =>
      val matchesLines = worldCoordinateMatchesLines.flatMap(worldCoordinates => toTileLine(tile, worldCoordinates))
      val tileDeviations = deviations.flatMap { deviation =>
        val lines = deviation.worldCoordinateLines.flatMap(worldCoordinates => toTileLine(tile, worldCoordinates))
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

      if (matchesLines.isEmpty && tileDeviations.isEmpty) {
        None
      }
      else {
        Some(
          MonitorStateTile(
            tile.z,
            tile.x,
            tile.y,
            tileDeviations,
            matchesLines
          )
        )
      }
    }
    stateTiles
  }

  private def buildWorldCoordinateMatchesLines(state: MonitorState): Seq[Seq[Coordinate]] = {
    state.matchesLines.map { line =>
      val coordinates = Json.value(line, classOf[CoordinateArray]).coordinates
      coordinates.toSeq.map { c =>
        val x = lonToWorldX(c.getX)
        val y = latToWorldY(c.getY)
        new Coordinate(x, y)
      }
    }
  }

  private def buildDeviations(state: MonitorState): Seq[MonitorStateDeviationWorldCoordinates] = {
    state.deviations.map { deviation =>
      val worldCoordinateLines = deviation.lines.map { line =>
        val coordinates = Json.value(line, classOf[CoordinateArray]).coordinates
        coordinates.toSeq.map { c =>
          val x = lonToWorldX(c.getX)
          val y = latToWorldY(c.getY)
          new Coordinate(x, y)
        }
      }
      MonitorStateDeviationWorldCoordinates(
        deviation.id,
        worldCoordinateLines
      )
    }
  }

  private def tilesForReference(worldCoordinateReferenceLines: Seq[Seq[Coordinate]]): Seq[Tile] = {
    worldCoordinateReferenceLines.flatMap { worldCoordinates =>
      val lineSegments = worldCoordinates
        .sliding(2)
        .map { case Seq(c1, c2) => new LineSegment(c1, c2) }
        .toSeq
      (ZoomLevel.newMinZoom to ZoomLevel.newMaxZoom).flatMap { z =>
        lineSegmentTileCalculator.tiles(z, lineSegments)
      }
    }
  }

  private def toTileLine(tile: Tile, worldCoordinates: Seq[Coordinate]): Option[String] = {
    val tileCoordinates = TileUtil.routeTileCoordinates(tile, worldCoordinates)
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
