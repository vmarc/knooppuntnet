package kpn.server.json.enumeratum

import kpn.api.common.Fact
import kpn.core.util.UnitTest
import kpn.server.json.Json

class FactJsonTest extends UnitTest {

  test("serializer") {
    Json.string(Fact.Added) should equal(""""Added"""")
  }

  test("deserializer") {
    val added = Json.value(""""Added"""", classOf[Fact])
    added should equal(Fact.Added)
  }
}
