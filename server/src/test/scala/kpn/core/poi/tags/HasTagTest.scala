package kpn.core.poi.tags

import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class HasTagTest extends UnitTest {

  test("HasTag") {

    val tags = Tags.from("key" -> "value")

    assert(HasTag("key").evaluate(tags))
    assert(HasTag("key", "value").evaluate(tags))

    assert(!HasTag("bla").evaluate(tags))
    assert(!HasTag("bla", "bla").evaluate(tags))
    assert(!HasTag("key", "bla").evaluate(tags))
  }
}
