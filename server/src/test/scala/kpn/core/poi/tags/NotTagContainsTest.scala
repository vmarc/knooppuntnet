package kpn.core.poi.tags

import kpn.api.custom.Tags
import org.scalatest.FunSuite
import org.scalatest.matchers.should.Matchers

class NotTagContainsTest extends FunSuite with Matchers {

  test("NotTagContains") {

    val tags1 = Tags.from("key" -> "value")
    val tags2 = Tags.from("key" -> "bla value bla")
    val tags3 = Tags.from("key" -> "bla")
    val tags4 = Tags.from("bla" -> "value")

    NotTagContains("key", "value").evaluate(tags1) should equal(false)
    NotTagContains("key", "value").evaluate(tags2) should equal(false)
    NotTagContains("key", "value").evaluate(tags3) should equal(true)
    NotTagContains("key", "value").evaluate(tags4) should equal(true)
  }
}
