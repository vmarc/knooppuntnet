package kpn.server.json

import kpn.shared.NetworkType
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NetworkTypeJsonDeserializerTest extends FunSuite with Matchers {

  test("deserializer") {
    val networkType = Json.value(""""cycling"""", classOf[NetworkType])
    networkType should equal(NetworkType.bicycle)
  }
}
