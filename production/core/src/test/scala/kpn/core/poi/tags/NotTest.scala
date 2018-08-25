package kpn.core.poi.tags

import kpn.shared.data.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NotTest extends FunSuite with Matchers {

  test("Not") {

    val tags = Tags.from("key" -> "value")

    HasTag("key").not.evaluate(tags) should equal(false)
    HasTag("bla").not.evaluate(tags) should equal(true)

  }
}
