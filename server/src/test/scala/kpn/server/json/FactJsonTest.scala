package kpn.server.json

import kpn.api.custom.Fact
import org.scalatest.FunSuite
import org.scalatest.Matchers

class FactJsonTest extends FunSuite with Matchers {

  test("serializer") {
    Json.string(Fact.Added) should equal(""""Added"""")
  }

  test("deserializer") {
    val added = Json.value(""""Added"""", classOf[Fact])
    added should equal(Fact.Added)
  }
}
