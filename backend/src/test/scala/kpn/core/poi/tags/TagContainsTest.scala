package kpn.core.poi.tags

import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class TagContainsTest extends UnitTest {

  test("TagContains") {

    val tags1 = Tags.from("key" -> "value")
    val tags2 = Tags.from("key" -> "bla value bla")
    val tags3 = Tags.from("key" -> "bla")
    val tags4 = Tags.from("bla" -> "value")

    assert(TagContains("key", "value").evaluate(tags1))
    assert(TagContains("key", "value").evaluate(tags2))
    assert(!TagContains("key", "value").evaluate(tags3))
    assert(!TagContains("key", "value").evaluate(tags4))
  }
}
