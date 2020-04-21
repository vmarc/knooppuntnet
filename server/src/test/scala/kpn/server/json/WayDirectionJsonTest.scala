package kpn.server.json

import kpn.api.common.route.Both
import kpn.api.common.route.WayDirection
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class WayDirectionJsonTest extends AnyFunSuite with Matchers {

  test("serializer") {
    Json.string(Both) should equal(""""Both"""")
  }

  test("deserializer") {
    val wayDirection = Json.value(""""Both"""", classOf[WayDirection])
    wayDirection should equal(Both)
  }
}
