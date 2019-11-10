package kpn.server.json

import kpn.api.custom.Fact
import org.scalatest.FunSuite
import org.scalatest.Matchers

class FactJsonSerializerTest extends FunSuite with Matchers {

  test("serializer") {
    Json.string(Fact.Added) should equal(""""Added"""")
  }
}
