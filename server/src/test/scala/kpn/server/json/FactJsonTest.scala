package kpn.server.json

import kpn.api.custom.Fact
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class FactJsonTest extends AnyFunSuite with Matchers {

  test("serializer") {
    Json.string(Fact.Added) should equal(""""Added"""")
  }

  test("deserializer") {
    val added = Json.value(""""Added"""", classOf[Fact])
    added should equal(Fact.Added)
  }
}
