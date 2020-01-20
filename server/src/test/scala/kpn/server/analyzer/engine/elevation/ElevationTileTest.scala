package kpn.server.analyzer.engine.elevation

import kpn.core.common.LatLonD
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ElevationTileTest extends FunSuite with Matchers {

  test("constructor") {
    ElevationTile(LatLonD(51, 4)) should equal(ElevationTile(51, 4, 1201, 0))
    ElevationTile(LatLonD(51.0 + (1199.0 / 1200), 4.0  + (1199.0 / 1200))) should equal(ElevationTile(51, 4, 2, 1199))
  }

  test("bounding rectangle") {
    val tile = ElevationTile(LatLonD(51, 4))

    tile.top.a.lat should equal(51.0)
    tile.top.a.lon should equal(4.0)
    tile.top.b.lat should equal(52.0)
    tile.top.b.lon should equal(4.0)

    tile.bottom.a.lat should equal(51.0)
    tile.bottom.a.lon should equal(5.0)
    tile.bottom.b.lat should equal(52.0)
    tile.bottom.b.lon should equal(5.0)

    tile.left.a.lat should equal(51.0)
    tile.left.a.lon should equal(4.0)
    tile.left.b.lat should equal(51.0)
    tile.left.b.lon should equal(5.0)

    tile.right.a.lat should equal(52.0)
    tile.right.a.lon should equal(4.0)
    tile.right.b.lat should equal(52.0)
    tile.right.b.lon should equal(5.0)

  }

}
