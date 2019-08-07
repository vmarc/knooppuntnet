// Migrated to Angular: filter-checkbox-group.component.ts
package kpn.client.components.filter

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.filter.FilterOptionGroup

import scalacss.ScalaCssReact._

object UiFilterCheckboxGroup {

  private case class Props(group: FilterOptionGroup)

  private val component = ScalaComponent.builder[Props]("checkbox-group")
    .render_P { props =>
      <.div(
        <.div(
          UiFilter.Styles.groupTitle,
          props.group.name
        ),
        props.group.options.toTagMod { option =>
          UiFilterCheckbox(option)
        }
      )
    }
    .build

  def apply(group: FilterOptionGroup): VdomElement = {
    component(Props(group))
  }
}
