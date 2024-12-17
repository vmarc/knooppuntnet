package kpn.server.json

import kpn.api.common.Country
import kpn.core.util.UnitTest

class CountryJsonTest extends UnitTest {

  test("serializer") {
    Json.string(Country.be) should equal(""""be"""")
    Json.string(Country.nl) should equal(""""nl"""")
    Json.string(Country.de) should equal(""""de"""")
    Json.string(Country.fr) should equal(""""fr"""")
    Json.string(Country.at) should equal(""""at"""")
  }

  test("deserializer") {
    Json.value(""""be"""", classOf[Country]) should equal(Country.be)
    Json.value(""""nl"""", classOf[Country]) should equal(Country.nl)
    Json.value(""""de"""", classOf[Country]) should equal(Country.de)
    Json.value(""""fr"""", classOf[Country]) should equal(Country.fr)
    Json.value(""""at"""", classOf[Country]) should equal(Country.at)
  }

  test("exception") {
    val message = intercept[NoSuchElementException] {
      Json.value(""""bla"""", classOf[Country])
    }.getMessage
    message should equal("bla is not a member of Enum (nl, be, de, fr, at, es, dk)")
  }
}
