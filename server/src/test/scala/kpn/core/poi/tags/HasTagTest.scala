package kpn.core.poi.tags

import kpn.shared.data.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers

class HasTagTest extends FunSuite with Matchers {

  test("HasTag") {

    val tags = Tags.from("key" -> "value")

    HasTag("key").evaluate(tags) should equal(true)
    HasTag("key", "value").evaluate(tags) should equal(true)

    HasTag("bla").evaluate(tags) should equal(false)
    HasTag("bla", "bla").evaluate(tags) should equal(false)
    HasTag("key", "bla").evaluate(tags) should equal(false)
  }
}
