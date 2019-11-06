package kpn.server.json

import kpn.shared.route.Both
import org.scalatest.FunSuite
import org.scalatest.Matchers

class WayDirectionJsonSerializerTest extends FunSuite with Matchers {

  test("serializer") {
    Json.string(Both) should equal(""""Both"""")
  }
}
