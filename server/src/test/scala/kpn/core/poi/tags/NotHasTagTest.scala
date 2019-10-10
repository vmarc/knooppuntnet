package kpn.core.poi.tags

import kpn.shared.data.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NotHasTagTest extends FunSuite with Matchers {

  test("NotHasTag") {

    val tags = Tags.from("key" -> "value")

    NotHasTag("key").evaluate(tags) should equal(false)
    NotHasTag("key", "value").evaluate(tags) should equal(false)

    NotHasTag("bla").evaluate(tags) should equal(true)
    NotHasTag("bla", "bla").evaluate(tags) should equal(true)
    NotHasTag("key", "bla").evaluate(tags) should equal(true)
  }
}
