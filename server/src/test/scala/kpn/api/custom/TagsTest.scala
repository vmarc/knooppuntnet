package kpn.api.custom

import kpn.api.common.data.Tagable
import kpn.core.util.UnitTest

class TagsTest extends UnitTest {

  private case class TestObject(tags: Seq[Tag]) extends Tagable

  test("hasTag") {

    assert(!TestObject(Seq.empty).hasTag("key"))

    assert(TestObject(Tags.from("key" -> "value")).hasTag("key"))
    assert(TestObject(Tags.from("key" -> "value")).hasTag("key", "value"))
    assert(!TestObject(Tags.from("key" -> "value")).hasTag("key", "value1", "value2"))

    assert(!TestObject(Tags.from("key" -> "value1;value2")).hasTag("key", "value"))
    assert(TestObject(Tags.from("key" -> "value1;value2")).hasTag("key", "value1"))
    assert(TestObject(Tags.from("key" -> "value1;value2")).hasTag("key", "value2"))
  }
}
