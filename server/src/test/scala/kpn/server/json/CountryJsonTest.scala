package kpn.server.json

import kpn.api.custom.Country
import org.scalatest.FunSuite
import org.scalatest.Matchers

class CountryJsonTest extends FunSuite with Matchers {

  test("serializer") {
    Json.string(Country.be) should equal(""""be"""")
  }

  test("deserializer") {
    val country = Json.value(""""be"""", classOf[Country])
    country should equal(Country.be)
  }
}
