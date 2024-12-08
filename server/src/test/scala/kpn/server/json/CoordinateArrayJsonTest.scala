package kpn.server.json

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.domain.CoordinateArray

class CoordinateArrayJsonTest extends UnitTest {

  test("deserializer") {
    val coordinates = Json.value("[[1.1,2.2],[3.3,4.4],[5.5,6.6]]", classOf[CoordinateArray]).coordinates
    coordinates.length should equal(3)
    coordinates(0).y should equal(1.1)
    coordinates(0).x should equal(2.2)
    coordinates(1).y should equal(3.3)
    coordinates(1).x should equal(4.4)
    coordinates(2).y should equal(5.5)
    coordinates(2).x should equal(6.6)
  }
}
