package kpn.server.json

import kpn.api.custom.NetworkScope
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NetworkScopeJsonDeserializerTest extends FunSuite with Matchers {

  test("deserializer") {
    val networkScope = Json.value(""""regional"""", classOf[NetworkScope])
    networkScope should equal(NetworkScope.regional)
  }
}
