package kpn.server.json

import kpn.api.custom.LocationRoutesType
import kpn.core.util.UnitTest

class LocationRoutesTypeJsonTest extends UnitTest {

  test("serializer") {
    Json.string(LocationRoutesType.all) should equal(""""all"""")
  }

  test("deserializer") {
    val locationRoutesType = Json.value(""""all"""", classOf[LocationRoutesType])
    locationRoutesType should equal(LocationRoutesType.all)
  }
}
