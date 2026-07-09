package kpn.core.poi.tags

import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class AndTest extends UnitTest {

  test("And") {

    val tags = Tags.from("key1" -> "value1", "key2" -> "value2")

    assert(HasTag("key1").and(HasTag("key2")).evaluate(tags))

    assert(!HasTag("bla").and(HasTag("key2")).evaluate(tags))
    assert(!HasTag("key1").and(HasTag("bla")).evaluate(tags))
    assert(!HasTag("bla").and(HasTag("bla")).evaluate(tags))
  }
}
