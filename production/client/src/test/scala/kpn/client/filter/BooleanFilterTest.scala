package kpn.client.filter

import org.scalatest.FunSuite
import org.scalatest.Matchers

class BooleanFilterTest extends FunSuite with Matchers {

  case class ExampleElement(property: Boolean, otherProperty: Option[String] = None)

  private def propertyAccessor(element: ExampleElement): Boolean = element.property
  private def otherPropertyAccessor(element: ExampleElement): Option[String] = element.otherProperty

  test("all") {

    val filter = new BooleanFilter[ExampleElement]("test", None, propertyAccessor)

    filter.passes(ExampleElement(property = true)) should equal(true)
    filter.passes(ExampleElement(property = false)) should equal(true)

    val allFilters = new Filters[ExampleElement](filter)

    val elements = Seq(
      ExampleElement(property = true),
      ExampleElement(property = true),
      ExampleElement(property = false)
    )

    val filterOptions = filter.filterOptions(allFilters, elements)

    filterOptions.get.name should equal("test")
    val Seq(all, yes, no) = filterOptions.get.options

    all should equal(FilterOption("all", 3, selected = true))
    yes should equal(FilterOption("yes", 2, selected = false))
    no should equal(FilterOption("no", 1, selected = false))
  }

  test("yes") {

    val filter = new BooleanFilter[ExampleElement]("test", Some(true), propertyAccessor)

    filter.passes(ExampleElement(property = true)) should equal(true)
    filter.passes(ExampleElement(property = false)) should equal(false)

    val allFilters = new Filters[ExampleElement](filter)

    val elements = Seq(
      ExampleElement(property = true),
      ExampleElement(property = true),
      ExampleElement(property = false)
    )

    val filterOptions = filter.filterOptions(allFilters, elements)

    filterOptions.get.name should equal("test")
    val Seq(all, yes, no) = filterOptions.get.options

    all should equal(FilterOption("all", 3, selected = false))
    yes should equal(FilterOption("yes", 2, selected = true))
    no should equal(FilterOption("no", 1, selected = false))
  }

  test("no") {

    val filter = new BooleanFilter[ExampleElement]("test", Some(false), propertyAccessor)

    filter.passes(ExampleElement(property = true)) should equal(false)
    filter.passes(ExampleElement(property = false)) should equal(true)

    val allFilters = new Filters[ExampleElement](filter)

    val elements = Seq(
      ExampleElement(property = true),
      ExampleElement(property = true),
      ExampleElement(property = false)
    )

    val filterOptions = filter.filterOptions(allFilters, elements)

    filterOptions.get.name should equal("test")
    val Seq(all, yes, no) = filterOptions.get.options

    all should equal(FilterOption("all", 3, selected = false))
    yes should equal(FilterOption("yes", 2, selected = false))
    no should equal(FilterOption("no", 1, selected = true))
  }

  test("all options disabled when one count zero") {

    val filter = new BooleanFilter[ExampleElement]("test", None, propertyAccessor)

    val allFilters = new Filters[ExampleElement](filter)

    val elements = Seq(
      ExampleElement(property = true),
      ExampleElement(property = true),
      ExampleElement(property = true)
    )

    filter.filterOptions(allFilters, elements) should equal(None)
  }

  test("take into account other filters") {

    val elements = Seq(
      ExampleElement(property = true, Some("A")),
      ExampleElement(property = true, Some("A")),
      ExampleElement(property = false, Some("A")),
      ExampleElement(property = true, Some("B")),
      ExampleElement(property = true, Some("B")),
      ExampleElement(property = true, Some("B")),
      ExampleElement(property = false, Some("B"))
    )

    {
      val Seq(all, yes, no) = filterResults(Seq())
      all should equal(FilterOption("all", 7, selected = false))
      yes should equal(FilterOption("yes", 5, selected = true))
      no should equal(FilterOption("no", 2, selected = false))
    }

    {
      val Seq(all, yes, no) = filterResults(Seq("A"))
      all should equal(FilterOption("all", 3, selected = false))
      yes should equal(FilterOption("yes", 2, selected = true))
      no should equal(FilterOption("no", 1, selected = false))
    }

    {
      val Seq(all, yes, no) = filterResults(Seq("B"))
      all should equal(FilterOption("all", 4, selected = false))
      yes should equal(FilterOption("yes", 3, selected = true))
      no should equal(FilterOption("no", 1, selected = false))
    }

    filterResults(Seq("C")) should equal(Seq.empty)

    def filterResults(otherFilterCriterium: Seq[String]): Seq[FilterOption] = {
      def updateState(strings: Seq[String]): Unit = {
      }

      val filter = new BooleanFilter[ExampleElement]("value", Some(true), propertyAccessor)
      val otherFilter = new StringFilter[ExampleElement]("category", otherFilterCriterium, otherPropertyAccessor, updateState)
      val allFilters = new Filters[ExampleElement](filter, otherFilter)
      val filterOptions = filter.filterOptions(allFilters, elements)
      filterOptions match {
        case None => Seq()
        case Some(group) => group.options
      }
    }
  }
}
