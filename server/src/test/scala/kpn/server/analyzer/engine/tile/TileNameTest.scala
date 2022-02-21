package kpn.server.analyzer.engine.tile

import kpn.core.util.UnitTest

class TileNameTest extends UnitTest {

  test("networkType") {
    TileName.networkType("hiking-10-002-003") should equal("hiking")
    TileName.networkType("cycling-10-002-003") should equal("cycling")
    TileName.networkType("horse-riding-10-002-003") should equal("horse-riding")
    TileName.networkType("canoe-10-002-003") should equal("canoe")
    TileName.networkType("motorboat-10-002-003") should equal("motorboat")
    TileName.networkType("inline-skating-10-002-003") should equal("inline-skating")
  }

  test("tileNumber") {
    TileName.tileNumber("hiking-10-002-003") should equal("10/002/003")
    TileName.tileNumber("cycling-10-002-003") should equal("10/002/003")
    TileName.tileNumber("horse-riding-10-002-003") should equal("10/002/003")
    TileName.tileNumber("canoe-10-002-003") should equal("10/002/003")
    TileName.tileNumber("motorboat-10-002-003") should equal("10/002/003")
    TileName.tileNumber("inline-skating-10-002-003") should equal("10/002/003")
  }

  test("tileZoomLevel") {
    TileName.tileZoomLevel("hiking-10-002-003") should equal(10)
    TileName.tileZoomLevel("cycling-10-002-003") should equal(10)
    TileName.tileZoomLevel("horse-riding-10-002-003") should equal(10)
    TileName.tileZoomLevel("canoe-10-002-003") should equal(10)
    TileName.tileZoomLevel("motorboat-10-002-003") should equal(10)
    TileName.tileZoomLevel("inline-skating-10-002-003") should equal(10)
  }
}
