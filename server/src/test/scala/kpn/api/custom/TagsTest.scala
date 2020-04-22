package kpn.api.custom

import kpn.api.common.data.Tagable
import kpn.core.util.UnitTest

class TagsTest extends UnitTest {

  private case class TestObject(tags: Tags) extends Tagable

  test("hasTag") {

    TestObject(Tags.empty).tags.has("key") should equal(false)

    TestObject(Tags.from("key" -> "value")).tags.has("key") should equal(true)
    TestObject(Tags.from("key" -> "value")).tags.has("key", "value") should equal(true)
    TestObject(Tags.from("key" -> "value")).tags.has("key", "value1", "value2") should equal(false)

    TestObject(Tags.from("key" -> "value1;value2")).tags.has("key", "value") should equal(false)
    TestObject(Tags.from("key" -> "value1;value2")).tags.has("key", "value1") should equal(true)
    TestObject(Tags.from("key" -> "value1;value2")).tags.has("key", "value2") should equal(true)
  }
}
