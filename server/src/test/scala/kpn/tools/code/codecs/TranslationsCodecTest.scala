package kpn.tools.code.codecs

import kpn.core.tools.translations.Translations
import kpn.core.util.UnitTest
import kpn.server.json.Json

import java.io.StringReader

class TranslationsCodecTest extends UnitTest {

  test("encode and decode translations") {

    val translations = Translations(
      locale = "nl",
      translations = Map(
        "key3" -> "value3",
        "key1" -> "value1",
        "key2" -> "value2",
      )
    )

    val json: String = Json.pretty(translations)

    val expectedJson =
      """{
        |  "locale": "nl",
        |  "translations": {
        |    "key1": "value1",
        |    "key2": "value2",
        |    "key3": "value3"
        |  }
        |}""".stripMargin

    assertEqual(
      json,
      expectedJson
    )

    val reader = new StringReader(json)
    val decoded = Json.readValue(reader, classOf[Translations])

    val expectedTranslations = Translations(
      locale = "nl",
      translations = Map(
        "key1" -> "value1",
        "key2" -> "value2",
        "key3" -> "value3",
      )
    )

    assertEqual(
      decoded,
      expectedTranslations
    )
  }
}
