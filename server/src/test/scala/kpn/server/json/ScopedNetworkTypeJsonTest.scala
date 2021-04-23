package kpn.server.json

import kpn.api.custom.ScopedNetworkType
import kpn.core.util.UnitTest

class ScopedNetworkTypeJsonTest extends UnitTest {

  test("serializer") {
    Json.string(ScopedNetworkType.rwn) should equal(""""rwn"""")
    Json.string(ScopedNetworkType.lwn) should equal(""""lwn"""")
    Json.string(ScopedNetworkType.rcn) should equal(""""rcn"""")
    Json.string(ScopedNetworkType.lcn) should equal(""""lcn"""")
  }

  test("deserializer") {
    deserialize("rwn") should equal(ScopedNetworkType.rwn)
    deserialize("lwn") should equal(ScopedNetworkType.lwn)
    deserialize("rcn") should equal(ScopedNetworkType.rcn)
    deserialize("lcn") should equal(ScopedNetworkType.lcn)
  }

  private def deserialize(string: String): ScopedNetworkType = {
    Json.value(s""""$string"""", classOf[ScopedNetworkType])
  }
}
