package kpn.server.json

import kpn.api.custom.NetworkScope
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class NetworkScopeJsonTest extends AnyFunSuite with Matchers {

  test("serializer") {
    Json.string(NetworkScope.regional) should equal(""""regional"""")
  }

  test("deserializer") {
    val networkScope = Json.value(""""regional"""", classOf[NetworkScope])
    networkScope should equal(NetworkScope.regional)
  }
}
