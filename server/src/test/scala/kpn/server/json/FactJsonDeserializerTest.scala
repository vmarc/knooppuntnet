package kpn.server.json

import kpn.api.custom.Fact
import org.scalatest.FunSuite
import org.scalatest.Matchers

class FactJsonDeserializerTest extends FunSuite with Matchers {

  test("deserializer") {
    val added = Json.value(""""Added"""", classOf[Fact])
    added should equal(Fact.Added)
  }
}
