package kpn.server.json

import kpn.api.custom.ScopedRouteType
import kpn.core.util.UnitTest

class ScopedRouteTypeJsonTest extends UnitTest {

  test("serializer") {
    Json.string(ScopedRouteType.rwn) should equal(""""rwn"""")
    Json.string(ScopedRouteType.lwn) should equal(""""lwn"""")
    Json.string(ScopedRouteType.rcn) should equal(""""rcn"""")
    Json.string(ScopedRouteType.lcn) should equal(""""lcn"""")
  }

  test("deserializer") {
    deserialize("rwn") should equal(ScopedRouteType.rwn)
    deserialize("lwn") should equal(ScopedRouteType.lwn)
    deserialize("rcn") should equal(ScopedRouteType.rcn)
    deserialize("lcn") should equal(ScopedRouteType.lcn)
  }

  private def deserialize(string: String): ScopedRouteType = {
    Json.value(s""""$string"""", classOf[ScopedRouteType])
  }
}
