package kpn.server.json

import kpn.api.common.RouteType
import kpn.core.util.UnitTest

class RouteTypeJsonTest extends UnitTest {

  test("serializer") {
    Json.string(RouteType.cycling) should equal(""""cycling"""")
    Json.string(RouteType.horseRiding) should equal(""""horse-riding"""")
    Json.string(RouteType.inlineSkating) should equal(""""inline-skating"""")
  }

  test("deserializer") {
    Json.value(""""cycling"""", classOf[RouteType]) should equal(RouteType.cycling)
    Json.value(""""horse-riding"""", classOf[RouteType]) should equal(RouteType.horseRiding)
    Json.value(""""inline-skating"""", classOf[RouteType]) should equal(RouteType.inlineSkating)
  }
}
