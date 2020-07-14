package kpn.server.analyzer.engine.elevation

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.domain.Point

class ElevationTileTest extends UnitTest {

  ignore("constructor") {
    ElevationTile(Point(51, 4)) should equal(ElevationTile(51, 4, 1201, 0))
    ElevationTile(Point(51.0 + (1199.0 / 1200), 4.0 + (1199.0 / 1200))) should equal(ElevationTile(51, 4, 2, 1199))
  }

  ignore("bounding rectangle") {
    val tile = ElevationTile(Point(51, 4))

    //    tile.top.p1.x should equal(51.0)
    //    tile.top.p1.y should equal(4.0)
    //    tile.top.p2.x should equal(52.0)
    //    tile.top.p2.y should equal(4.0)
    //
    //    tile.bottom.p1.x should equal(51.0)
    //    tile.bottom.p1.y should equal(5.0)
    //    tile.bottom.p2.x should equal(52.0)
    //    tile.bottom.p2.y should equal(5.0)
    //
    //    tile.left.p1.x should equal(51.0)
    //    tile.left.p1.y should equal(4.0)
    //    tile.left.p2.x should equal(51.0)
    //    tile.left.p2.y should equal(5.0)
    //
    //    tile.right.p1.x should equal(52.0)
    //    tile.right.p1.y should equal(4.0)
    //    tile.right.p2.x should equal(52.0)
    //    tile.right.p2.y should equal(5.0)
  }

}
