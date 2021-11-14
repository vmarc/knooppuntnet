package kpn.server.json

import kpn.api.common.DE
import kpn.api.common.EN
import kpn.api.common.FR
import kpn.api.common.Language
import kpn.api.common.NL
import kpn.core.util.UnitTest

class LanguageJsonTest extends UnitTest {

  test("serializer") {
    Json.string(EN) should equal(""""en"""")
    Json.string(NL) should equal(""""nl"""")
    Json.string(DE) should equal(""""de"""")
    Json.string(FR) should equal(""""fr"""")
  }

  test("deserializer") {
    Json.value(""""en"""", classOf[Language]) should equal(EN)
    Json.value(""""nl"""", classOf[Language]) should equal(NL)
    Json.value(""""de"""", classOf[Language]) should equal(DE)
    Json.value(""""fr""""", classOf[Language]) should equal(FR)
  }
}
