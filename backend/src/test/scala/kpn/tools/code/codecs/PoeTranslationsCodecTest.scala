package kpn.tools.code.codecs

import kpn.core.tools.translations.PoeTranslations
import kpn.core.util.UnitTest
import kpn.server.json.Json

import java.io.StringReader

class PoeTranslationsCodecTest extends UnitTest {

  test("encode and decode poe translations") {

    val translations = PoeTranslations(
      translations = Map(
        "key3" -> "value3",
        "key1" -> "value1",
        "key2" -> "value2",
      )
    )

    val json = Json.pretty(translations)

    println(json)

    assertEqual(
      json,
      """{
        |  "key1": "value1",
        |  "key2": "value2",
        |  "key3": "value3"
        |}""".stripMargin
    )

    val reader = new StringReader(json)
    val decoded = Json.readValue(reader, classOf[PoeTranslations])

    val expectedTranslations = PoeTranslations(
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
