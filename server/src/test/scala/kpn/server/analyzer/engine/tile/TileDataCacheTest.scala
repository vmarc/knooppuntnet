package kpn.server.analyzer.engine.tile

import kpn.core.util.UnitTest

class TileDataCacheTest extends UnitTest {

  test("cache") {
    val cache = new TileDataCache[String]()

    cache.getOrElseUpdate(1, None) should equal(None)
    cache.getOrElseUpdate(1, Some("one")) should equal(Some("one"))
    cache.getOrElseUpdate(1, None) should equal(Some("one"))

    cache.clear()
    cache.getOrElseUpdate(1, None) should equal(None)
  }
}
