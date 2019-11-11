package kpn.server.json

import kpn.api.custom.NetworkScope
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NetworkScopeJsonTest extends FunSuite with Matchers {

  test("serializer") {
    Json.string(NetworkScope.regional) should equal(""""regional"""")
  }

  test("deserializer") {
    val networkScope = Json.value(""""regional"""", classOf[NetworkScope])
    networkScope should equal(NetworkScope.regional)
  }
}
