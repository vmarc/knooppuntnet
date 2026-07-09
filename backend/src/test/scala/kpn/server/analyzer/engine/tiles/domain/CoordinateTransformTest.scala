package kpn.server.analyzer.engine.tiles.domain

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.worldYtoLat

class CoordinateTransformTest extends UnitTest {

  test("coordinate transform") {
    assertTransform(0, 0, 0.5, 0.5)
    assertTransform(0, -180, 0, 0.5)
    assertTransform(0, 180, 1, 0.5)
    assertTransform(0, 180 - 1e-7, 1, 0.5)
    assertTransform(45, 0, 0.5, 0.3597)

    worldYtoLat(-0.1) should equal(87.35 +- 0.01)
    worldYtoLat(1.1) should equal(-87.35 +- 0.01)
  }

  private def assertTransform(lat: Double, lon: Double, worldX: Double, worldY: Double): Unit = {
    CoordinateTransform.latToWorldY(lat) should equal(worldY +- 0.0001)
    CoordinateTransform.lonToWorldX(lon) should equal(worldX +- 0.0001)
    CoordinateTransform.worldYtoLat(worldY) should equal(lat +- 0.01)
    CoordinateTransform.worldXtoLon(worldX) should equal(lon +- 0.01)
  }
}
