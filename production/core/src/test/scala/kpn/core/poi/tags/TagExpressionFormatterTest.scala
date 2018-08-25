package kpn.core.poi.tags

import org.scalatest.FunSuite
import org.scalatest.Matchers

class TagExpressionFormatterTest extends FunSuite with Matchers {

  test("HasTag(key)") {
    val expression = HasTag("key")
    new TagExpressionFormatter().format(expression) should equal("[key]")
  }

  test("HasTag(key, value)") {
    val expression = HasTag("key", "value")
    new TagExpressionFormatter().format(expression) should equal("[key=value]")
  }

  test("HasTag(key1).and(HasTag(key2)") {
    val expression = HasTag("key1").and(HasTag("key2"))
    new TagExpressionFormatter().format(expression) should equal("[key1][key2]")
  }

  test("HasTag(key1, value1).and(HasTag(key2, value2)") {
    val expression = HasTag("key1", "value1").and(HasTag("key2", "value2"))
    new TagExpressionFormatter().format(expression) should equal("[key1=value1][key2=value2]")
  }

  test("HasTag(key, value1, value2)") {
    val expression = HasTag("key", "value1", "value2")
    new TagExpressionFormatter().format(expression) should equal("[key~'value1|value2']")
  }

  test("NotHasTag(key)") {
    val expression = NotHasTag("key")
    new TagExpressionFormatter().format(expression) should equal("[key!~'.']")
  }

  test("NotHasTag(key, value)") {
    val expression = NotHasTag("key", "value")
    new TagExpressionFormatter().format(expression) should equal("[key!~'value']")
  }

  test("NotHasTag(key, value1, value2, value3)") {
    val expression = NotHasTag("key", "value1", "value2", "value3")
    new TagExpressionFormatter().format(expression) should equal("[key!~'value1|value2|value3']")
  }

}
