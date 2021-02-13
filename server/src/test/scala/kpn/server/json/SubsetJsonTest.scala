package kpn.server.json

import kpn.api.custom.Subset
import kpn.core.util.UnitTest

class SubsetJsonTest extends UnitTest {

  test("serializer") {
    Json.string(Subset.nlHiking) should equal("""{"country":"nl","networkType":"hiking"}""")
  }

  test("deserializer") {
    val subset = Json.value("""{"country":"nl","networkType":"hiking"}""", classOf[Subset])
    subset should equal(Subset.nlHiking)
  }

  test("backward compatible deserializer") {
    val subset = Json.value(""""nl:hiking"""", classOf[Subset])
    subset should equal(Subset.nlHiking)
  }
}
