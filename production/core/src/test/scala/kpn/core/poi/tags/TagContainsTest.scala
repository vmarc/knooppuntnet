package kpn.core.poi.tags

import kpn.shared.data.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers

class TagContainsTest extends FunSuite with Matchers {

  test("TagContains") {

    val tags1 = Tags.from("key" -> "value")
    val tags2 = Tags.from("key" -> "bla value bla")
    val tags3 = Tags.from("key" -> "bla")
    val tags4 = Tags.from("bla" -> "value")

    TagContains("key", "value").evaluate(tags1) should equal(true)
    TagContains("key", "value").evaluate(tags2) should equal(true)
    TagContains("key", "value").evaluate(tags3) should equal(false)
    TagContains("key", "value").evaluate(tags4) should equal(false)
  }
}
