package kpn.server.analyzer.engine.tile

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.TileTestSetup
import kpn.server.analyzer.engine.tiles.domain.TileDataRouteSegment

class RouteTileCalculatorTest extends UnitTest {

  val t = new TileTestSetup()

  val linesTileCalculator = new LineSegmentTileCalculatorImpl(t.tileCalculator)
  val calculator = new RouteTileCalculatorImpl(linesTileCalculator)

  test("single tile route") {

    val b = t.t22.tile.bounds

    val delta = (b.xMax - b.xMin) / 4
    val x1 = b.xMin + delta
    val x2 = b.xMax - delta
    val y1 = b.yMin + delta
    val y2 = b.yMax - delta

    val worldCoordinates = Seq(x1, y1, x2, y2)

    val tiles = calculator.tiles(
      13,
      Seq(
        TileDataRouteSegment(
          0,
          0,
          Seq.empty,
          oneWay = false,
          "",
          worldCoordinates
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

    val worldCoordinates = Seq(tile1.xCenter, tile1.yCenter, tile2.xCenter, tile2.yCenter)

    val tiles = calculator.tiles(
      13,
      Seq(
        TileDataRouteSegment(
          0,
          0,
          Seq.empty,
          oneWay = false,
          "",
          worldCoordinates
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

    val worldCoordinates = Seq(tile1.xCenter, tile1.yCenter, tile2.xCenter, tile2.yCenter)

    val tiles = calculator.tiles(
      13,
      Seq(
        TileDataRouteSegment(
          0,
          0,
          Seq.empty,
          oneWay = false,
          "",
          worldCoordinates
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
