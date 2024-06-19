package kpn.server.json

import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class TagsJsonTest extends UnitTest {

  test("tag") {
    val tag1 = Tag("key1", "value1")
    val json = Json.string(tag1)
    val tag2 = Json.value(json, classOf[Tag])
    json should equal("""{"key":"key1","value":"value1"}""")
    tag1.shouldMatchTo(tag2)
  }

  test("quotes should be escaped") {
    val tags = Tags.from("key1" -> """a "b" c""")
    val json = Json.string(tags)
    json should equal("""[{"key":"key1","value":"a \"b\" c"}]""")
  }

  test("keys and values are unescaped") {
    val tags = Json.value("""{"key":"key1","value":"a \"b\" c"}""", classOf[Tag])
    tags should equal(Tag("key1", """a "b" c"""))
  }
}
