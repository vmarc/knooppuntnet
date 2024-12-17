package kpn.server.json

import kpn.api.common.Language
import kpn.core.util.UnitTest

class LanguageJsonTest extends UnitTest {

  test("serializer") {
    Json.string(Language.EN) should equal(""""en"""")
    Json.string(Language.NL) should equal(""""nl"""")
    Json.string(Language.DE) should equal(""""de"""")
    Json.string(Language.FR) should equal(""""fr"""")
  }

  test("deserializer") {
    Json.value(""""en"""", classOf[Language]) should equal(Language.EN)
    Json.value(""""nl"""", classOf[Language]) should equal(Language.NL)
    Json.value(""""de"""", classOf[Language]) should equal(Language.DE)
    Json.value(""""fr""""", classOf[Language]) should equal(Language.FR)
  }
}
