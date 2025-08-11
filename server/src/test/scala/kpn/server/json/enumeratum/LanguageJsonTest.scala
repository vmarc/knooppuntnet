package kpn.server.json.enumeratum

import kpn.api.common.Language
import kpn.core.util.UnitTest
import kpn.server.json.Json

class LanguageJsonTest extends UnitTest {

  test("serializer") {
    Json.string(Language.en) should equal(""""en"""")
    Json.string(Language.nl) should equal(""""nl"""")
    Json.string(Language.de) should equal(""""de"""")
    Json.string(Language.fr) should equal(""""fr"""")
  }

  test("deserializer") {
    Json.value(""""en"""", classOf[Language]) should equal(Language.en)
    Json.value(""""nl"""", classOf[Language]) should equal(Language.nl)
    Json.value(""""de"""", classOf[Language]) should equal(Language.de)
    Json.value(""""fr""""", classOf[Language]) should equal(Language.fr)
  }
}
