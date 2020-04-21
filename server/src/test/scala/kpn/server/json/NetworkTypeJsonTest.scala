package kpn.server.json

import kpn.api.custom.NetworkType
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class NetworkTypeJsonTest extends AnyFunSuite with Matchers {

  test("serializer") {
    Json.string(NetworkType.cycling) should equal(""""cycling"""")
  }

  test("deserializer") {
    val networkType = Json.value(""""cycling"""", classOf[NetworkType])
    networkType should equal(NetworkType.cycling)
  }
}
