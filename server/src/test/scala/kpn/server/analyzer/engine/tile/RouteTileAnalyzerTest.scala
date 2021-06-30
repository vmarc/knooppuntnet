package kpn.server.analyzer.engine.tile

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.TestTileSetup
import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.Point
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.analyzer.engine.tiles.domain.TileRouteSegment

class RouteTileAnalyzerTest extends UnitTest {

  val t = new TestTileSetup()

  val analyzer = new RouteTileAnalyzerImpl(t.tileCalculator)

  test("single tile route") {

    val b = t.t22.tile.bounds

    val delta = (b.xMax - b.xMin) / 4
    val x1 = b.xMin + delta
    val x2 = b.xMax - delta
    val y1 = b.yMin + delta
    val y2 = b.yMax - delta

    val line = Line(Point(x1, y1), Point(x2, y2))

    val tileRoute = TileDataRoute(
      routeId = 1L,
      "01-02",
      "layer",
      None,
      None,
      segments = Seq(
        TileRouteSegment(
          0,
          oneWay = false,
          "",
          lines = Seq(
            line
          )
        )
      )
    )

    val tiles = analyzer.tiles(13, tileRoute)

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

    val tileRoute = TileDataRoute(
      routeId = 1L,
      "01-02",
      "layer",
      None,
      None,
      segments = Seq(
        TileRouteSegment(
          0,
          oneWay = false,
          "",
          lines = Seq(
            line
          )
        )
      )
    )

    val tiles = analyzer.tiles(13, tileRoute)

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

    val tileRoute = TileDataRoute(
      routeId = 1L,
      "01-02",
      "layer",
      None,
      None,
      segments = Seq(
        TileRouteSegment(
          0,
          oneWay = false,
          "",
          lines = Seq(
            line
          )
        )
      )
    )

    val tiles = analyzer.tiles(13, tileRoute)

    tiles.map(_.name).toSet should equal(
      Set(
        t.t12.tile.name,
        t.t22.tile.name,
        t.t32.tile.name
      )
    )
  }
}
