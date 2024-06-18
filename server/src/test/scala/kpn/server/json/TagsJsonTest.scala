package kpn.server.json

import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class TagsJsonTest extends UnitTest {

  test("serializer") {
    val tags = Tags.from("key1" -> "value1", "key2" -> "value2", "key3" -> "value3")
    val json = Json.string(tags)
    json should equal("""[{"key":"key1","value":"value1"},{"key":"key2","value":"value2"},{"key":"key3","value":"value3"}]""")
  }

  test("quotes should be escaped") {
    val tags = Tags.from("key1" -> """a "b" c""")
    val json = Json.string(tags)
    json should equal("""[{"key":"key1","value":"a \"b\" c"}]""")
  }

  test("deserializer") {
    val tags = Json.value("""{"key":"key1","value":"value1"}""", classOf[Tag])
    tags should equal(Tag("key1", "value1"))
  }

  test("keys and values are unescaped") {
    val tags = Json.value("""{"key":"key1","value":"a \"b\" c"}""", classOf[Tag])
    tags should equal(Tag("key1", """a "b" c"""))
  }
}
