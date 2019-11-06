package kpn.server.json

import kpn.shared.route.Both
import kpn.shared.route.WayDirection
import org.scalatest.FunSuite
import org.scalatest.Matchers

class WayDirectionJsonDeserializerTest extends FunSuite with Matchers {

  test("deserializer") {
    val wayDirection = Json.value(""""Both"""", classOf[WayDirection])
    wayDirection should equal(Both)
  }
}
