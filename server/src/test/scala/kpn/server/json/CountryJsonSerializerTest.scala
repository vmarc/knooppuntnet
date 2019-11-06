package kpn.server.json

import kpn.shared.Country
import org.scalatest.FunSuite
import org.scalatest.Matchers

class CountryJsonSerializerTest extends FunSuite with Matchers {

  test("serializer") {
    Json.string(Country.be) should equal(""""be"""")
  }
}
