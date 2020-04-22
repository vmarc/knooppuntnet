package kpn.server.json

import kpn.api.custom.Subset
import kpn.core.util.UnitTest

class SubsetJsonTest extends UnitTest {

  test("serializer") {
    Json.string(Subset.nlHiking) should equal(""""nl:hiking"""")
  }

  test("deserializer") {
    val subset = Json.value(""""nl:hiking"""", classOf[Subset])
    subset should equal(Subset.nlHiking)
  }
}
