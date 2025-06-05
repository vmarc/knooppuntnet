package kpn.server.analyzer.engine.tile

import kpn.api.common.LatLon
import kpn.api.common.LatLonImpl
import kpn.core.test.Locations
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.TestTile
import kpn.server.analyzer.engine.tiles.TileTestSetup
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform

class NodeTileCalculatorTest extends UnitTest {

  val t = new TileTestSetup()

  val calculator = new NodeTileCalculatorImpl(t.routeTileCache)

  test("calculate all tiles for given node") {

    assertEqual(calculateLatLon(Locations.essen), tileNames(t.center))

    val delta = 0.0005

    val centerTile = t.center.tile.bounds

    val centerLat = Locations.essen.lat
    val centerLon = Locations.essen.lon

    val left = CoordinateTransform.worldXtoLon(centerTile.xMin) //+ delta
    val right = CoordinateTransform.worldXtoLon(centerTile.xMax) //- delta
    val top = CoordinateTransform.worldYtoLat(centerTile.yMin) //- delta
    val bottom = CoordinateTransform.worldYtoLat(centerTile.yMax) //+ delta

    assertEqual(calculate(centerLat, left), tileNames(t.center, t.west))
    assertEqual(calculate(centerLat, right), tileNames(t.center, t.east))
    assertEqual(calculate(top, centerLon), tileNames(t.center, t.north))
    assertEqual(calculate(bottom, centerLon), tileNames(t.center, t.south))

    assertEqual(calculate(top, left), tileNames(t.northWest, t.north, t.west, t.center))
    assertEqual(calculate(top, right), tileNames(t.north, t.northEast, t.center, t.east))
    assertEqual(calculate(bottom, left), tileNames(t.west, t.center, t.southWest, t.south))
    assertEqual(calculate(bottom, right), tileNames(t.center, t.east, t.south, t.southEast))
  }

  private def calculate(lat: Double, lon: Double): Seq[String] = {
    calculateLatLon(LatLonImpl.from(lat, lon))
  }

  private def calculateLatLon(latLon: LatLon): Seq[String] = {
    val calculated = calculator.tiles(t.zoomLevel, latLon)
    calculated.map(_.name).sorted
  }

  private def tileNames(tiles: TestTile*): Seq[String] = {
    tiles.map(_.tile.name).sorted
  }
}
