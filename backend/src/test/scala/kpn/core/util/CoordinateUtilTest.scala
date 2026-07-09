package kpn.core.util

import kpn.api.common.LatLon
import kpn.api.common.LatLonImpl

class CoordinateUtilTest extends UnitTest {

  test("formatCoordinate") {
    CoordinateUtil.formatCoordinate(0) should equal("0")
    CoordinateUtil.formatCoordinate(-0) should equal("0")
    CoordinateUtil.formatCoordinate(0.0000000000001) should equal("0")
    CoordinateUtil.formatCoordinate(0.1234567890123456) should equal("0.12345679")
    CoordinateUtil.formatCoordinate(0.12345678123456) should equal("0.12345678")
    CoordinateUtil.formatCoordinate(123456.789) should equal("123456.789")
  }

  test("toCoordinates") {
    val latLons: Seq[LatLon] = Seq(
      LatLonImpl("0", "0"),
      LatLonImpl("1", "2"),
    )
    CoordinateUtil.toCoordinates(latLons) should equal(
      "[[0,0],[222638.98158655,111325.14286638]]"
    )
  }

  test("string to coordinates") {
    val coordinates = CoordinateUtil.stringToCoordinates("[[1.1,2.2],[3.3,4.4]")
    coordinates.length should equal(2)
    val coordinate1 = coordinates.head
    val coordinate2 = coordinates(1)
    coordinate1.x should equal(1.1)
    coordinate1.y should equal(2.2)
    coordinate2.x should equal(3.3)
    coordinate2.y should equal(4.4)
  }
}
