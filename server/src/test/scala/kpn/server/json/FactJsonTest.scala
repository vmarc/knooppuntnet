package kpn.server.json

import kpn.api.custom.Fact
import kpn.core.util.UnitTest

class FactJsonTest extends UnitTest {

  test("serializer") {
    Json.string(Fact.Added) should equal(""""Added"""")
  }

  test("deserializer") {
    val added = Json.value(""""Added"""", classOf[Fact])
    added should equal(Fact.Added)
  }
}
