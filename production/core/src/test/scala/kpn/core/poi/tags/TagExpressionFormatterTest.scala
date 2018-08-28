package kpn.core.poi.tags

import org.scalatest.FunSuite
import org.scalatest.Matchers

class TagExpressionFormatterTest extends FunSuite with Matchers {

  test("HasTag(key)") {
    assertExpression(HasTag("key"), "[key]")
  }

  test("HasTag(key, value)") {
    assertExpression(HasTag("key", "value"), "[key=value]")
  }

  test("HasTag(key1).and(HasTag(key2)") {
    assertExpression(HasTag("key1").and(HasTag("key2")), "[key1][key2]")
  }

  test("HasTag(key1, value1).and(HasTag(key2, value2)") {
    assertExpression(HasTag("key1", "value1").and(HasTag("key2", "value2")), "[key1=value1][key2=value2]")
  }

  test("HasTag(key, value1, value2)") {
    assertExpression(HasTag("key", "value1", "value2"), "[key~'^value1$|^value2$']")
  }

  test("NotHasTag(key)") {
    assertExpression(NotHasTag("key"), "[key!~'.']")
  }

  test("NotHasTag(key, value)") {
    assertExpression(NotHasTag("key", "value"), "[key!=value]")
  }

  test("TagContains(key, value)") {
    assertExpression(TagContains("key", "value"), "[key~'value']")
  }

  test("TagContains(key, value1, value2)") {
    assertExpression(TagContains("key", "value1", "value2"), "[key~'value1|value2']")
  }

  test("NotTagContains(key, value)") {
    assertExpression(NotTagContains("key", "value"), "[key!~'value']")
  }

  test("NotTagContains(key, value1, value2, value3)") {
    assertExpression(NotTagContains("key", "value1", "value2", "value3"), "[key!~'value1|value2|value3']")
  }

  private def assertExpression(expression: TagExpression, expectedFormattedExpressionString: String): Unit = {
    new TagExpressionFormatter().format(expression) should equal(expectedFormattedExpressionString)
  }

}
