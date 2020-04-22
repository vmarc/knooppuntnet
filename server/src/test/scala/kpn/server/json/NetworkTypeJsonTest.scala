package kpn.server.json

import kpn.api.custom.NetworkType
import kpn.core.util.UnitTest

class NetworkTypeJsonTest extends UnitTest {

  test("serializer") {
    Json.string(NetworkType.cycling) should equal(""""cycling"""")
  }

  test("deserializer") {
    val networkType = Json.value(""""cycling"""", classOf[NetworkType])
    networkType should equal(NetworkType.cycling)
  }
}
