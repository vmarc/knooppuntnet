package kpn.server.monitor.route.update

import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileUtil
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.domain.MonitorStateTile
import kpn.server.monitor.domain.MonitorStateTileDeviation
import org.locationtech.jts.geom.Coordinate
import org.springframework.stereotype.Component

@Component
class MonitorStateBuilder(
  lineSegmentTileCalculator: LineSegmentTileCalculator
) {

  def build(state: MonitorState): MonitorState = {
    val stateTiles = buildStateTiles(state)
    state.copy(tiles = stateTiles)
  }

  private def buildStateTiles(state: MonitorState): Seq[MonitorStateTile] = {
    val worldCoordinateMatchesLines = buildWorldCoordinateMatchesLines(state)
    val deviations = buildDeviations(state)
    val allWorldCoordinateReferenceLines = worldCoordinateMatchesLines ++ deviations.flatMap(_.worldCoordinateLines)
    val tiles = lineSegmentTileCalculator.tilesForLines(allWorldCoordinateReferenceLines)
    tiles.flatMap { tile =>
      buildTile(worldCoordinateMatchesLines, deviations, tile)
    }
  }

  private def buildTile(
    worldCoordinateMatchesLines: Seq[Seq[Coordinate]],
    deviations: Seq[MonitorStateDeviationWorldCoordinates],
    tile: Tile
  ): Option[MonitorStateTile] = {

    val matchesLines = worldCoordinateMatchesLines.flatMap(worldCoordinates => TileUtil.toTileLine(tile, worldCoordinates))
    val tileDeviations = buildTileDeviations(tile, deviations)

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
