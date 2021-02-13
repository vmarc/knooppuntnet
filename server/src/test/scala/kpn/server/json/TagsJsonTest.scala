package kpn.server.json

import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class TagsJsonTest extends UnitTest {

  test("serializer") {
    val tags = Tags.from("key1" -> "value1", "key2" -> "value2", "key3" -> "value3")
    val json = Json.string(tags)
    json should equal("""{"tags":[{"key":"key1","value":"value1"},{"key":"key2","value":"value2"},{"key":"key3","value":"value3"}],"empty":false}""")
  }

  test("quotes should be escaped") {
    val tags = Tags.from("key1" -> """a "b" c""")
    val json = Json.string(tags)
    json should equal("""{"tags":[{"key":"key1","value":"a \"b\" c"}],"empty":false}""")
  }

  test("deserializer") {
    val tags = Json.value("""{"tags":[{"key":"key1","value":"value1"},{"key":"key2","value":"value2"},{"key":"key3","value":"value3"}],"empty":false}""", classOf[Tags])
    tags should equal(Tags.from("key1" -> "value1", "key2" -> "value2", "key3" -> "value3"))
  }

  test("keys and values are unescaped") {
    val tags = Json.value("""{"tags":[{"key":"key1","value":"a \"b\" c"}],"empty":false}""", classOf[Tags])
    tags should equal(Tags.from("""a "b" c""" -> """d "e" f"""))
  }

  test("backward compatible deserializer") {
    val tags = Json.value("""[["key1","value1"],["key2","value2"],["key3","value3"]]""", classOf[Tags])
    tags should equal(Tags.from("key1" -> "value1", "key2" -> "value2", "key3" -> "value3"))
  }

  test("backward compatible deserializer - keys and values are unescaped") {
    val tags = Json.value("""[["a \"b\" c","d \"e\" f"]]""", classOf[Tags])
    tags should equal(Tags.from("""a "b" c""" -> """d "e" f"""))
  }

}
