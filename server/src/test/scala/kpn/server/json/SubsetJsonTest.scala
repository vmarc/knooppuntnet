package kpn.server.json

import kpn.api.custom.Subset
import org.scalatest.FunSuite
import org.scalatest.Matchers

class SubsetJsonTest extends FunSuite with Matchers {

  test("serializer") {
    Json.string(Subset.nlHiking) should equal(""""nl:hiking"""")
  }

  test("deserializer") {
    val subset = Json.value(""""nl:hiking"""", classOf[Subset])
    subset should equal(Subset.nlHiking)
  }
}
