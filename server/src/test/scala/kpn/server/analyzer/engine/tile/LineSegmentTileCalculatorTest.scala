package kpn.server.analyzer.engine.tile

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.TileTestSetup
import org.locationtech.jts.geom.LineSegment

class LineSegmentTileCalculatorTest extends UnitTest {

  val t = new TileTestSetup()

  val linesTileCalculator = new LineSegmentTileCalculatorImpl(t.tileCalculator)

  test("single tile route") {

    val b = t.center.tile.bounds

    val delta = (b.xMax - b.xMin) / 4
    val x1 = b.xMin + delta
    val x2 = b.xMax - delta
    val y1 = b.yMin + delta
    val y2 = b.yMax - delta

    val lineSegment = new LineSegment(x1, y1, x2, y2)
    val tiles = linesTileCalculator.tiles(13, Seq(lineSegment))

    tiles.map(_.name).toSet should equal(
      Set(
        t.center.tile.name
      )
    )
  }

  test("route traversing 2 tiles") {

    val tile1 = t.west.tile.bounds
    val tile2 = t.center.tile.bounds

    val lineSegment = new LineSegment(tile1.xCenter, tile1.yCenter, tile2.xCenter, tile2.yCenter)
    val tiles = linesTileCalculator.tiles(13, Seq(lineSegment))

    tiles.map(_.name).toSet should equal(
      Set(
        t.west.tile.name,
        t.center.tile.name
      )
    )
  }

  test("route traversing 3 tiles") {

    val tile1 = t.west.tile.bounds
    val tile2 = t.east.tile.bounds

    val lineSegment = new LineSegment(tile1.xCenter, tile1.yCenter, tile2.xCenter, tile2.yCenter)
    val tiles = linesTileCalculator.tiles(13, Seq(lineSegment))

    tiles.map(_.name).toSet should equal(
      Set(
        t.west.tile.name,
        t.center.tile.name,
        t.east.tile.name
      )
    )
  }
}
