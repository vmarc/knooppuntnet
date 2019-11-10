package kpn.server.json

import kpn.api.common.route.Both
import org.scalatest.FunSuite
import org.scalatest.Matchers

class WayDirectionJsonSerializerTest extends FunSuite with Matchers {

  test("serializer") {
    Json.string(Both) should equal(""""Both"""")
  }
}
