// Migrated to Angular: boolean-filter.ts
package kpn.client.filter

import japgolly.scalajs.react.Callback

class BooleanFilter[T](
  name: String,
  val criterium: Option[Boolean],
  val booleanPropertyAccessor: (T) => Boolean,
  all: Callback = Callback.empty,
  yes: Callback = Callback.empty,
  no: Callback = Callback.empty
) extends Filter[T](name) {

  def passes(element: T): Boolean = {
    if(criterium.isEmpty) {
      true
    }
    else {
      if(criterium.get) {
        booleanPropertyAccessor(element)
      }
      else {
        ! booleanPropertyAccessor(element)
      }
    }
  }

  def filterOptions(allFilters: Filters[T], elements: Seq[T]): Option[FilterOptionGroup] = {

    val filteredElements = allFilters.filterExcept(elements, this)
    val (yesElements, noElements) = filteredElements.partition(booleanPropertyAccessor)
    val active = filteredElements.nonEmpty && yesElements.nonEmpty && noElements.nonEmpty

    if (active) {
      val allOption = FilterOption(
        "all",
        filteredElements.size,
        criterium.isEmpty,
        all
      )

      val yesOption = FilterOption(
        "yes",
        yesElements.size,
        criterium.contains(true),
        yes
      )

      val noOption = FilterOption(
        "no",
        noElements.size,
        criterium.contains(false),
        no
      )

      Some(FilterOptionGroup(name, allOption, yesOption, noOption))
    }
    else {
      None
    }
  }
}
