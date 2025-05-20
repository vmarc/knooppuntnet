package kpn.server.analyzer.engine.tiles.domain

import kpn.core.test.Locations
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import org.locationtech.jts.geom.Coordinate

class TileTest extends UnitTest {

  test("Tile") {

    val z = 13
    val x = 4197
    val y = 2725

    Tile.tileX(z, lonToWorldX(Locations.essen.lon)) should equal(x)
    Tile.tileY(z, latToWorldY(Locations.essen.lat)) should equal(y)

    val tile = Tile.routeTile(z, x, y)

    tile.worldXMin should equal(lonToWorldX(4.43847) +- 0.001)
    tile.worldXMax should equal(lonToWorldX(4.48242) +- 0.001)
    tile.worldYMin should equal(latToWorldY(51.45400) +- 0.001)
    tile.worldYMax should equal(latToWorldY(51.48138) +- 0.001)

    tile.clipBounds.xMin should equal(lonToWorldX(4.43607) +- 0.001)
    tile.clipBounds.xMax should equal(lonToWorldX(4.48482) +- 0.001)
    tile.clipBounds.yMin should equal(latToWorldY(51.45250) +- 0.001)
    tile.clipBounds.yMax should equal(latToWorldY(51.48288) +- 0.001)

    val worldX = lonToWorldX(Locations.essen.lon)
    val worldY = latToWorldY(Locations.essen.lat)
    val worldCoordinate = new Coordinate(worldX, worldY)
    val scaled = tile.scale(worldCoordinate)

    println(scaled)
  }

  test("contains") {
    val tile = Tile.routeTile(1, 0, 0)

    tile.bounds.xMin should equal(0.0)
    tile.bounds.xMax should equal(0.5)
    tile.bounds.yMin should equal(0)
    tile.bounds.yMax should equal(0.5)

    tile.contains(Seq(0.2, 0.2, 0.4, 0.4)) should equal(true)
    tile.contains(Seq(-0.25, 0.25, 0.25, 0.25)) should equal(true)
    tile.contains(Seq(-0.25, -0.25, 0.75, 0.75)) should equal(true)
    tile.contains(Seq(-0.25, 0.75, 0.25, 0.75)) should equal(false)
  }
}
