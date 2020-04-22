package kpn.core.poi.tags

import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class AndTest extends UnitTest {

  test("And") {

    val tags = Tags.from("key1" -> "value1", "key2" -> "value2")

    HasTag("key1").and(HasTag("key2")).evaluate(tags) should equal(true)

    HasTag("bla").and(HasTag("key2")).evaluate(tags) should equal(false)
    HasTag("key1").and(HasTag("bla")).evaluate(tags) should equal(false)
    HasTag("bla").and(HasTag("bla")).evaluate(tags) should equal(false)
  }
}
