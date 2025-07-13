package kpn.server.monitor.route.update

import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileUtil
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorReferenceTile
import org.locationtech.jts.geom.Coordinate
import org.springframework.stereotype.Component

@Component
class MonitorReferenceBuilder(
  lineSegmentTileCalculator: LineSegmentTileCalculator
) {

  def build(reference: MonitorReference): MonitorReference = {
    val referenceTiles = buildReferenceTiles(reference)
    reference.copy(tiles = referenceTiles)
  }

  private def buildReferenceTiles(reference: MonitorReference): Seq[MonitorReferenceTile] = {
    val worldCoordinateReferenceLines = buildWorldCoordinateReferenceLines(reference)
    val tiles = lineSegmentTileCalculator.tilesForLines(worldCoordinateReferenceLines)
    buildTiles(tiles, worldCoordinateReferenceLines)
  }

  private def buildWorldCoordinateReferenceLines(reference: MonitorReference): Seq[Seq[Coordinate]] = {
    reference.referenceLines.map(CoordinateTransform.lineToWorldCoordinates)
  }

  private def buildTiles(tiles: Seq[Tile], worldCoordinateReferenceLines: Seq[Seq[Coordinate]]): Seq[MonitorReferenceTile] = {
    tiles.flatMap { tile =>
      val lines = worldCoordinateReferenceLines.flatMap(worldCoordinates => TileUtil.toTileLine(tile, worldCoordinates))
      if (lines.isEmpty) {
        None
      }
      else {
        Some(
          MonitorReferenceTile(
            tile.z,
            tile.x,
            tile.y,
            lines
          )
        )
      }
    }
  }
}
