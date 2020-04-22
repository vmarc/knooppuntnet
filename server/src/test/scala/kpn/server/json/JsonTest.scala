package kpn.server.json

import kpn.core.util.UnitTest

class JsonTest extends UnitTest {

  test("case class json") {
    val example = JsonExample("John Doe", 123)
    val json = Json.string(example)
    json should equal("""{"name":"John Doe","age":123}""")
    Json.value(json, classOf[JsonExample]) should equal(example)
  }

  test("quotes in strings are escaped in json") {
    val example = JsonExample("""John "F" Doe""", 123)
    val json = Json.string(example)
    json should equal("""{"name":"John \"F\" Doe","age":123}""")
    Json.value(json, classOf[JsonExample]) should equal(example)
  }

}
