package kpn.server.json

import kpn.shared.Country
import org.scalatest.FunSuite
import org.scalatest.Matchers

class CountryJsonDeserializerTest extends FunSuite with Matchers {

  test("deserializer") {
    val country = Json.value(""""be"""", classOf[Country])
    country should equal(Country.be)
  }
}
