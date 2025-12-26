package kpn.server.analyzer.engine.tile

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.TileTestSetup
import kpn.server.analyzer.engine.tiles.domain.Tile
import org.locationtech.jts.geom.LineSegment

class LineSegmentTileCalculatorTest extends UnitTest {

  private val t = new TileTestSetup()
  private val linesTileCalculator = new LineSegmentTileCalculator(t.routeTileCache)

  test("single tile route in center tile") {

    val b = t.center.bounds

    val delta = (b.xMax - b.xMin) / 4
    val x1 = b.xMin + delta
    val x2 = b.xMax - delta
    val y1 = b.yMin + delta
    val y2 = b.yMax - delta

    val lineSegment = new LineSegment(x1, y1, x2, y2)
    val tiles = linesTileCalculator.tiles(13, Seq(lineSegment))

    tiles.map(_.name).toSet should equal(
      Set(
        t.center.name
      )
    )
  }

  test("route traversing 2 tiles - west to center") {
    assertEqual(
      tilesForLineSegmentThroughCentersOfTiles(t.west, t.center),
      tileNames(
        t.west,
        t.center
      )
    )
  }

  test("route traversing 2 tiles - north to center") {
    assertEqual(
      tilesForLineSegmentThroughCentersOfTiles(t.north, t.center),
      tileNames(
        t.north,
        t.center
      )
    )
  }

  test("route traversing 2 tiles - center to south") {
    assertEqual(
      tilesForLineSegmentThroughCentersOfTiles(t.center, t.south),
      tileNames(
        t.center,
        t.south
      )
    )
  }

  test("route traversing 3 tiles - west to east") {
    assertEqual(
      tilesForLineSegmentThroughCentersOfTiles(t.west, t.east),
      tileNames(
        t.west,
        t.center,
        t.east
      )
    )
  }

  test("route traversing 3 tiles - north to south") {
    assertEqual(
      tilesForLineSegmentThroughCentersOfTiles(t.north, t.south),
      tileNames(
        t.north,
        t.center,
        t.south
      )
    )
  }

  private def tilesForLineSegmentThroughCentersOfTiles(tile1: Tile, tile2: Tile): Seq[String] = {
    val lineSegment = lineSegmentThroughCentersOfTiles(tile1, tile2)
    linesTileCalculator.tiles(13, Seq(lineSegment)).map(_.name).sorted
  }

  private def lineSegmentThroughCentersOfTiles(tile1: Tile, tile2: Tile): LineSegment = {
    new LineSegment(
      tile1.bounds.xCenter,
      tile1.bounds.yCenter,
      tile2.bounds.xCenter,
      tile2.bounds.yCenter
    )
  }

  private def tileNames(tiles: Tile*): Seq[String] = {
    tiles.map(_.name).sorted
  }
}
