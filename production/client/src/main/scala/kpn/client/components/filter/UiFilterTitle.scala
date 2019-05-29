// TODO migrate to Angular
package kpn.client.components.filter

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.filter.FilterOptions

import scalacss.ScalaCssReact._

object UiFilterTitle {

  private case class Props(filterOptions: FilterOptions)

  private val component = ScalaComponent.builder[Props]("filter-title")
    .render_P { props =>
      val filteredCount = props.filterOptions.filteredCount
      val totalCount = props.filterOptions.totalCount
      <.div(
        UiFilter.Styles.row,
        <.div(
          UiFilter.Styles.title,
          "Filter"
        ),
        <.div(
          UiFilter.Styles.total,
          filteredCount + "/" + totalCount
        )
      )
    }
    .build

  def apply(filterOptions: FilterOptions): VdomElement = {
    component(Props(filterOptions))
  }
}
