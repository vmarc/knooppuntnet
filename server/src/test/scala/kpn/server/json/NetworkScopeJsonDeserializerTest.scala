package kpn.server.json

import kpn.shared.NetworkScope
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NetworkScopeJsonDeserializerTest extends FunSuite with Matchers {

  test("deserializer") {
    val networkScope = Json.value(""""regional"""", classOf[NetworkScope])
    networkScope should equal(NetworkScope.regional)
  }
}
