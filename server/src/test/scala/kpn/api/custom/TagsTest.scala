package kpn.api.custom

import kpn.api.common.data.Tagable
import kpn.core.util.UnitTest

class TagsTest extends UnitTest {

  private case class TestObject(tags: Seq[Tag]) extends Tagable

  test("hasTag - false when tag is missing") {
    TestObject(Seq.empty).hasTag("key") should equal(false)
  }

  test("hasTag - tag with single value") {
    val taggable = taggableWithTagValue("value")
    taggable.hasTag("key") should equal(true)
    taggable.hasTag("key", "value") should equal(true)
    taggable.hasTag("key", "bla") should equal(false)
  }

  test("hasTag - tag with multiple values") {
    val taggable = taggableWithTagValue("value1;value2")
    taggable.hasTag("key", "value1") should equal(true)
    taggable.hasTag("key", "value2") should equal(true)
    taggable.hasTag("key", "bla") should equal(false)
  }

  test("values - no values when tag is missing") {
    TestObject(Seq.empty).tagValues("key") should equal(Seq.empty)
  }

  test("values - no values when tag value is empty") {
    taggableWithTagValue("").tagValues("key") should equal(Seq.empty)
  }

  test("values") {
    taggableWithTagValue("value").tagValues("key") should equal(Seq("value"))
    taggableWithTagValue("  value  ").tagValues("key") should equal(Seq("value"))
    taggableWithTagValue("value1;value2").tagValues("key") should equal(Seq("value1", "value2"))
    taggableWithTagValue("value1;;value2").tagValues("key") should equal(Seq("value1", "value2"))
    taggableWithTagValue("value1  ;  ;  value2").tagValues("key") should equal(Seq("value1", "value2"))
  }

  private def taggableWithTagValue(value: String): TestObject = {
    TestObject(Tags.from("key" -> value))
  }
}
