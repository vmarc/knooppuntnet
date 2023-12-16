package kpn.server.analyzer.engine.tiles.domain

import kpn.api.common.LatLonImpl
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX

class TileTest extends UnitTest {

  test("Tile") {

    val z = 13
    val x = 4197
    val y = 2725

    val essen = LatLonImpl("51.46774", "4.46839")

    Tile.tileX(z, lonToWorldX(essen.lon)) should equal(x)
    Tile.tileY(z, latToWorldY(essen.lat)) should equal(y)

    val tile = Tile(z, x, y)

    tile.worldXMin should equal(lonToWorldX(4.43847) +- 0.001)
    tile.worldXMax should equal(lonToWorldX(4.48242) +- 0.001)
    tile.worldYMin should equal(latToWorldY(51.45400) +- 0.001)
    tile.worldYMax should equal(latToWorldY(51.48138) +- 0.001)

    tile.clipBounds.xMin should equal(lonToWorldX(4.43607) +- 0.001)
    tile.clipBounds.xMax should equal(lonToWorldX(4.48482) +- 0.001)
    tile.clipBounds.yMin should equal(latToWorldY(51.45250) +- 0.001)
    tile.clipBounds.yMax should equal(latToWorldY(51.48288) +- 0.001)
  }
}
