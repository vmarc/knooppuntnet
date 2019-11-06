package kpn.server.json

import kpn.shared.Subset
import org.scalatest.FunSuite
import org.scalatest.Matchers

class SubsetJsonSerializerTest extends FunSuite with Matchers {

  test("serializer") {
    Json.string(Subset.nlHiking) should equal(""""nl:hiking"""")
  }
}
