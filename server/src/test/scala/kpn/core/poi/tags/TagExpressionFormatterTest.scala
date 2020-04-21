package kpn.core.poi.tags

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TagExpressionFormatterTest extends AnyFunSuite with Matchers {

  test("HasTag(key)") {
    assertExpression(HasTag("key"), Seq("[key]"))
  }

  test("HasTag(key, value)") {
    assertExpression(HasTag("key", "value"), Seq("[key=value]"))
  }

  test("HasTag(key1).and(HasTag(key2)") {
    assertExpression(HasTag("key1").and(HasTag("key2")), Seq("[key1][key2]"))
  }

  test("HasTag(key1, value1).and(HasTag(key2, value2)") {
    assertExpression(HasTag("key1", "value1").and(HasTag("key2", "value2")), Seq("[key1=value1][key2=value2]"))
  }

  test("HasTag(key, value1, value2)") {
    assertExpression(HasTag("key", "value1", "value2"), Seq("[key~'^value1$|^value2$']"))
  }

  test("NotHasTag(key)") {
    assertExpression(NotHasTag("key"), Seq("[key!~'.']"))
  }

  test("NotHasTag(key, value)") {
    assertExpression(NotHasTag("key", "value"), Seq("[key!=value]"))
  }

  test("TagContains(key, value)") {
    assertExpression(TagContains("key", "value"), Seq("[key~'value']"))
  }

  test("TagContains(key, value1, value2)") {
    assertExpression(TagContains("key", "value1", "value2"), Seq("[key~'value1|value2']"))
  }

  test("NotTagContains(key, value)") {
    assertExpression(NotTagContains("key", "value"), Seq("[key!~'value']"))
  }

  test("NotTagContains(key, value1, value2, value3)") {
    assertExpression(NotTagContains("key", "value1", "value2", "value3"), Seq("[key!~'value1|value2|value3']"))
  }

  test("or") {
    assertExpression(HasTag("tourism", "picnic_site").or(HasTag("leisure", "picnic_table")), Seq("[tourism=picnic_site]", "[leisure=picnic_table]"))
  }

  private def assertExpression(expression: TagExpression, expectedFormattedExpressionStrings: Seq[String]): Unit = {
    new TagExpressionFormatter().format(expression) should equal(expectedFormattedExpressionStrings)
  }

}
