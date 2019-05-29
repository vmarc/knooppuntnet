// TODO migrate to Angular
package kpn.client.filter

import org.scalatest.FunSuite
import org.scalatest.Matchers

class StringFilterTest extends FunSuite with Matchers {

  case class ExampleElement(property: Option[String])

  private def propertyAccessor(element: ExampleElement): Option[String] = element.property

  test("all") {

    def updateState(strings: Seq[String]): Unit = {
    }

    val filter = new StringFilter[ExampleElement]("name", Seq(), propertyAccessor, updateState)

    filter.passes(ExampleElement(Some("A"))) should equal(true)
    filter.passes(ExampleElement(Some("B"))) should equal(true)
    filter.passes(ExampleElement(Some("C"))) should equal(true)
    filter.passes(ExampleElement(None)) should equal(true)
  }

  test("value") {

    def updateState(strings: Seq[String]): Unit = {
    }

    val filter = new StringFilter[ExampleElement]("name", Seq("A", "B"), propertyAccessor, updateState)

    filter.passes(ExampleElement(Some("A"))) should equal(true)
    filter.passes(ExampleElement(Some("C"))) should equal(false)
    filter.passes(ExampleElement(None)) should equal(false)
  }
}
