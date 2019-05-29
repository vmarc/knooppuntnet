// TODO migrate to Angular
package kpn.client.filter

import japgolly.scalajs.react.CallbackTo

class StringFilter[T](
  name: String,
  strings: Seq[String],
  elementPropertyAccessor: (T) => Option[String],
  updateState: (Seq[String]) => Unit
) extends Filter[T](name) {

  def passes(element: T): Boolean = {
    strings.isEmpty || (elementPropertyAccessor(element) match {
      case Some(value) => strings.contains(value)
      case _ => false
    })
  }

  def filterOptions(allFilters: Filters[T], elements: Seq[T]): Option[FilterOptionGroup] = {

    val filteredElements = allFilters.filterExcept(elements, this)
    val stringCounts = filteredElements.map(elementPropertyAccessor).flatten.groupBy(x => x).map(xx => xx._1 -> xx._2.size).toSeq.sortBy(_._1).reverse
    if (stringCounts.nonEmpty) {
      val stringFilterOptions = stringCounts.map { stringCount =>
        val selected = strings.contains(stringCount._1)
        val newStrings: Seq[String] = if (selected) {
          strings.filterNot(_ == stringCount._1)
        }
        else {
          strings :+ stringCount._1
        }

        val callback = CallbackTo {
          updateState(newStrings)
        }

        FilterOption(stringCount._1, stringCount._2, selected, callback)
      }

      val options = stringFilterOptions.sortWith { (a, b) =>
        if (a.count == b.count) {
          a.name < b.name
        }
        else {
          a.count > b.count
        }
      }

      Some(FilterOptionGroup(name, options: _*))
    }
    else {
      None
    }
  }
}
