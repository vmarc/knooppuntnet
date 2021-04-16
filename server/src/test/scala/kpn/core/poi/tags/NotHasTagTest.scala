package kpn.core.poi.tags

import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class NotHasTagTest extends UnitTest {

  test("NotHasTag") {

    val tags = Tags.from("key" -> "value")

    assert(!NotHasTag("key").evaluate(tags))
    assert(!NotHasTag("key", "value").evaluate(tags))

    assert(NotHasTag("bla").evaluate(tags))
    assert(NotHasTag("bla", "bla").evaluate(tags))
    assert(NotHasTag("key", "bla").evaluate(tags))
  }
}
