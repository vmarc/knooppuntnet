package kpn.server.json

import kpn.api.custom.NetworkScope
import kpn.core.util.UnitTest

class NetworkScopeJsonTest extends UnitTest {

  test("serializer") {
    Json.string(NetworkScope.regional) should equal(""""regional"""")
  }

  test("deserializer") {
    val networkScope = Json.value(""""regional"""", classOf[NetworkScope])
    networkScope should equal(NetworkScope.regional)
  }
}
