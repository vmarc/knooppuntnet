package kpn.server.json

import kpn.api.custom.NetworkType
import kpn.core.util.UnitTest

class NetworkTypeJsonTest extends UnitTest {

  test("serializer") {
    Json.string(NetworkType.cycling) should equal(""""cycling"""")
    Json.string(NetworkType.horseRiding) should equal(""""horse-riding"""")
    Json.string(NetworkType.inlineSkating) should equal(""""inline-skating"""")
  }

  test("deserializer") {
    Json.value(""""cycling"""", classOf[NetworkType]) should equal(NetworkType.cycling)
    Json.value(""""horse-riding"""", classOf[NetworkType]) should equal(NetworkType.horseRiding)
    Json.value(""""inline-skating"""", classOf[NetworkType]) should equal(NetworkType.inlineSkating)
  }
}
