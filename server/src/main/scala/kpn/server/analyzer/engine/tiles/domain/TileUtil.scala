package kpn.server.analyzer.engine.tiles.domain

import org.locationtech.jts.geom.LineSegment

object TileUtil {

  def top(tile: Tile): LineSegment = {
    new LineSegment(
      tile.worldXMin,
      tile.worldYMin,
      tile.worldXMax,
      tile.worldYMin
    )
  }

  def bottom(tile: Tile): LineSegment = {
    new LineSegment(
      tile.worldXMin,
      tile.worldYMax,
      tile.worldXMax,
      tile.worldYMax
    )
  }

  def left(tile: Tile): LineSegment = {
    new LineSegment(
      tile.worldXMin,
      tile.worldYMin,
      tile.worldXMin,
      tile.worldYMax
    )
  }

  def right(tile: Tile): LineSegment = {
    new LineSegment(
      tile.worldXMax,
      tile.worldYMin,
      tile.worldXMax,
      tile.worldYMin
    )
  }
}
