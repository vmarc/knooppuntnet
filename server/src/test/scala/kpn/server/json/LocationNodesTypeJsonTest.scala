package kpn.server.json

import kpn.api.custom.LocationNodesType
import kpn.core.util.UnitTest

class LocationNodesTypeJsonTest extends UnitTest {

  test("serializer") {
    Json.string(LocationNodesType.all) should equal(""""all"""")
  }

  test("deserializer") {
    val locationNodesType = Json.value(""""all"""", classOf[LocationNodesType])
    locationNodesType should equal(LocationNodesType.all)
  }
}
