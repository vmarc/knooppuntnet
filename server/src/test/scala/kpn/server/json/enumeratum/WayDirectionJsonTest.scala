package kpn.server.json.enumeratum

import kpn.api.common.route.WayDirection
import kpn.api.common.route.WayDirection.Both
import kpn.core.util.UnitTest
import kpn.server.json.Json

class WayDirectionJsonTest extends UnitTest {

  test("serializer") {
    Json.string(Both) should equal(""""both"""")
  }

  test("deserializer") {
    val wayDirection = Json.value(""""both"""", classOf[WayDirection])
    wayDirection should equal(Both)
  }
}
