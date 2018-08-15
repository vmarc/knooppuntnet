package kpn.core.db.json

import kpn.core.db.json.JsonFormats._
import kpn.shared.data.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers
import spray.json._

class TagsFormatTest extends FunSuite with Matchers {

  test("tags") {
    val tags = Tags.from("key1" -> "value1", "key2" -> "value2", "key3" -> "value3")
    val json = tags.toJson

    json.convertTo[Tags] should equal(tags)
  }
}
