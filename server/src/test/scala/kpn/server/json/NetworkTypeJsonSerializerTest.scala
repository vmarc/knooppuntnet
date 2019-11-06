package kpn.server.json

import kpn.shared.NetworkType
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NetworkTypeJsonSerializerTest extends FunSuite with Matchers {

  test("serializer") {
    Json.string(NetworkType.bicycle) should equal(""""cycling"""")
  }
}
