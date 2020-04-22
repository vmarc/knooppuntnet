package kpn.server.json

import kpn.api.common.route.Both
import kpn.api.common.route.WayDirection
import kpn.core.util.UnitTest

class WayDirectionJsonTest extends UnitTest {

  test("serializer") {
    Json.string(Both) should equal(""""Both"""")
  }

  test("deserializer") {
    val wayDirection = Json.value(""""Both"""", classOf[WayDirection])
    wayDirection should equal(Both)
  }
}
