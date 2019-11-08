package kpn.server.json

import kpn.shared.data.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers

class TagsJsonSerializerTest extends FunSuite with Matchers {

  test("serializer") {
    val tags = Tags.from("key1" -> "value1", "key2" -> "value2", "key3" -> "value3")
    val json = Json.string(tags)
    json should equal("""[["key1","value1"],["key2","value2"],["key3","value3"]]""")
  }

  test("quotes should be escaped") {
    val tags = Tags.from("key1" -> """a "b" c""")
    val json = Json.string(tags)
    json should equal("""[["key1","a \"b\" c"]]""")
  }
}
