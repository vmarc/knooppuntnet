package kpn.server.json

import kpn.api.custom.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers

class TagsJsonDeserializerTest extends FunSuite with Matchers {

  test("deserializer") {
    val tags = Json.value("""[["key1","value1"],["key2","value2"],["key3","value3"]]""", classOf[Tags])
    tags should equal(Tags.from("key1" -> "value1", "key2" -> "value2", "key3" -> "value3"))
  }

  test("keys and values are unescaped") {
    val tags = Json.value("""[["a \"b\" c","d \"e\" f"]]""", classOf[Tags])
    tags should equal(Tags.from("""a "b" c""" -> """d "e" f"""))
  }

}
