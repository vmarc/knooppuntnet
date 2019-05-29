// TODO migrate to Angular
package kpn.client.components.filter

import chandu0101.scalajs.react.components.materialui.MuiCheckbox
import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.ReactMouseEvent
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.VdomNode
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.components.common.OnDomRendered
import kpn.client.filter.FilterOption

import scala.scalajs.js
import scalacss.ScalaCssReact._

object UiFilterCheckbox {

  private case class Props(option: FilterOption)

  private case class State(selected: Boolean = false)

  private class Backend(scope: BackendScope[Props, State]) {

    def render(props: Props, state: State): VdomElement = {

      val check = (event: ReactMouseEvent, checked: Boolean) => {
        scope.setState(
          State(!state.selected),
          OnDomRendered {
            // small delay so that checkbox selection gets updated in the browser window
            scope.props.runNow().option.updateState.runNow() // TODO should use callback chaining here?
          }
        )
      }

      val count: VdomNode = <.span(
        UiFilter.Styles.optionCount,
        props.option.count
      )

      val checkBoxLabel: VdomNode = <.span(
        props.option.name,
        count
      )

      <.div(
        MuiCheckbox(
          key = props.option.name,
          checked = state.selected,
          iconStyle = js.Dynamic.literal(
            fill = "#000"
          ),
          label = checkBoxLabel,
          onCheck = check
        )()
      )
    }
  }

  private val component = ScalaComponent.builder[Props]("filter-option")
    .initialState(State())
    .renderBackend[Backend]
    .componentWillMount { scope =>
      scope.setState(State(scope.props.option.selected))
    }
    .componentWillReceiveProps { scope =>
      scope.setState(State(scope.nextProps.option.selected))
    }
    .build

  def apply(option: FilterOption): VdomElement = {
    component(Props(option))
  }
}
