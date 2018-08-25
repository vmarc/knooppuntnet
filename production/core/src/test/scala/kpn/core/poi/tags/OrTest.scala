package kpn.core.poi.tags

import kpn.shared.data.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers

class OrTest  extends FunSuite with Matchers {

  test("Or") {

    val tags = Tags.from("key1" -> "value1", "key2" -> "value2")

    HasTag("key1").or(HasTag("key2")).evaluate(tags) should equal(true)
    HasTag("bla").or(HasTag("key2")).evaluate(tags) should equal(true)
    HasTag("key1").or(HasTag("bla")).evaluate(tags) should equal(true)
    HasTag("bla").or(HasTag("bla")).evaluate(tags) should equal(false)
  }
}
