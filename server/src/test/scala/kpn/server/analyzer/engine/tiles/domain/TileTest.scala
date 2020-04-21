package kpn.server.analyzer.engine.tiles.domain

import kpn.api.common.LatLonImpl
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TileTest extends AnyFunSuite with Matchers {

  test("Tile") {

    val z = 13
    val x = 4197
    val y = 2725

    val essen = LatLonImpl("51.46774", "4.46839")

    Tile.x(z, essen.lon) should equal(x)
    Tile.y(z, essen.lat) should equal(y)

    val tile = new Tile(z, x, y)

    tile.bounds.xMin should equal(4.43847 +- 0.00001)
    tile.bounds.xMax should equal(4.48242 +- 0.00001)
    tile.bounds.yMin should equal(51.45400 +- 0.00001)
    tile.bounds.yMax should equal(51.48138 +- 0.00001)

    tile.clipBounds.xMin should equal(4.43607 +- 0.00001)
    tile.clipBounds.xMax should equal(4.48482 +- 0.00001)
    tile.clipBounds.yMin should equal(51.45250 +- 0.00001)
    tile.clipBounds.yMax should equal(51.48288 +- 0.00001)
  }
}
