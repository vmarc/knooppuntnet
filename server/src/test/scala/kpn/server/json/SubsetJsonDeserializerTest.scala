package kpn.server.json

import kpn.shared.Subset
import org.scalatest.FunSuite
import org.scalatest.Matchers

class SubsetJsonDeserializerTest extends FunSuite with Matchers {

  test("deserializer") {
    val subset = Json.value(""""nl:hiking"""", classOf[Subset])
    subset should equal(Subset.nlHiking)
  }
}
