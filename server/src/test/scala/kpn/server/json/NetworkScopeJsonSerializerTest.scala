package kpn.server.json

import kpn.shared.NetworkScope
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NetworkScopeJsonSerializerTest extends FunSuite with Matchers {

  test("serializer") {
    Json.string(NetworkScope.regional) should equal(""""regional"""")
  }
}
