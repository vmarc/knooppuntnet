package kpn.server.analyzer.engine.tile

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.TestTileSetup
import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.Point
import kpn.server.analyzer.engine.tiles.domain.RouteTileSegment

class RouteTileCalculatorTest extends UnitTest {

  val t = new TestTileSetup()

  val linesTileCalculator = new LinesTileCalculatorImpl(t.tileCalculator)
  val calculator = new RouteTileCalculatorImpl(linesTileCalculator)

  test("single tile route") {

    val b = t.t22.tile.bounds

    val delta = (b.xMax - b.xMin) / 4
    val x1 = b.xMin + delta
    val x2 = b.xMax - delta
    val y1 = b.yMin + delta
    val y2 = b.yMax - delta

    val line = Line(Point(x1, y1), Point(x2, y2))

    val tiles = calculator.tiles(
      13,
      Seq(
        RouteTileSegment(
          0,
          oneWay = false,
          "",
          lines = Seq(
            line
          )
        )
      )
    )

    tiles.map(_.name).toSet should equal(
      Set(
        t.t22.tile.name
      )
    )
  }

  test("route traversing 2 tiles") {

    val tile1 = t.t12.tile.bounds
    val tile2 = t.t22.tile.bounds

    val line = Line(Point(tile1.xCenter, tile1.yCenter), Point(tile2.xCenter, tile2.yCenter))

    val tiles = calculator.tiles(
      13,
      Seq(
        RouteTileSegment(
          0,
          oneWay = false,
          "",
          lines = Seq(
            line
          )
        )
      )
    )

    tiles.map(_.name).toSet should equal(
      Set(
        t.t12.tile.name,
        t.t22.tile.name
      )
    )
  }

  test("route traversing 3 tiles") {

    val tile1 = t.t12.tile.bounds
    val tile2 = t.t32.tile.bounds

    val line = Line(Point(tile1.xCenter, tile1.yCenter), Point(tile2.xCenter, tile2.yCenter))

    val tiles = calculator.tiles(
      13,
      Seq(
        RouteTileSegment(
          0,
          oneWay = false,
          "",
          lines = Seq(
            line
          )
        )
      )
    )

    tiles.map(_.name).toSet should equal(
      Set(
        t.t12.tile.name,
        t.t22.tile.name,
        t.t32.tile.name
      )
    )
  }
}
