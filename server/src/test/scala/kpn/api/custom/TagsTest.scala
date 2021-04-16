package kpn.api.custom

import kpn.api.common.data.Tagable
import kpn.core.util.UnitTest

class TagsTest extends UnitTest {

  private case class TestObject(tags: Tags) extends Tagable

  test("hasTag") {

    assert(!TestObject(Tags.empty).tags.has("key"))

    assert(TestObject(Tags.from("key" -> "value")).tags.has("key"))
    assert(TestObject(Tags.from("key" -> "value")).tags.has("key", "value"))
    assert(!TestObject(Tags.from("key" -> "value")).tags.has("key", "value1", "value2"))

    assert(!TestObject(Tags.from("key" -> "value1;value2")).tags.has("key", "value"))
    assert(TestObject(Tags.from("key" -> "value1;value2")).tags.has("key", "value1"))
    assert(TestObject(Tags.from("key" -> "value1;value2")).tags.has("key", "value2"))
  }
}
