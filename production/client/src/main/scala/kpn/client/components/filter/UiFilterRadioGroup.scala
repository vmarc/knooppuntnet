package kpn.client.components.filter

import chandu0101.scalajs.react.components.Implicits._
import chandu0101.scalajs.react.components.materialui.MuiRadioButton
import chandu0101.scalajs.react.components.materialui.MuiRadioButtonGroup
import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.ReactEvent
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.VdomNode
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.components.common.OnDomRendered
import kpn.client.filter.FilterOption
import kpn.client.filter.FilterOptionGroup

import scala.scalajs.js
import scalacss.ScalaCssReact._

object UiFilterRadioGroup {

  private case class Props(group: FilterOptionGroup)

  private case class State(selected: Option[FilterOption] = None)

  private class Backend(scope: BackendScope[Props, State]) {

    var selected: Option[FilterOption] = scope.props.runNow().group.options.find(_.selected)

    private def onOptionClick(option: FilterOption): Unit = {
      selected = Some(option)
      scope.setState(
        State(selected),
        OnDomRendered {
          // small delay so that radio button selection gets updated in the browser window
          selected.get.updateState.runNow()
        }
      ).runNow()
    }

    def render(props: Props, state: State): VdomElement = {

      val selected: String = props.group.options.find(_.selected).get.name

      def changeAction(e: ReactEvent, sel: String): Callback = {
        props.group.options.find(_.name == sel).get.updateState
      }

      val options: Seq[VdomNode] = props.group.options.map { option =>

        val button: VdomNode = MuiRadioButton[String](
          key = props.group.name + "/" + option.name,
          iconStyle = js.Dynamic.literal(
            fill = "#000"
          ),
          value = option.name,
          disabled = option.isEmpty,
          label = <.span(
            option.name,
            <.span(
              UiFilter.Styles.optionCount,
              option.count
            )
          )
        )()
        button
      }

      <.div(
        props.group.name,
        MuiRadioButtonGroup[String](
          key = props.group.name,
          name = props.group.name,
          defaultSelected = js.use(selected).as[js.Any],
          onChange = changeAction _
        )(
          options: _*
        )
      )
    }
  }

  private val component = ScalaComponent.builder[Props]("filter-group")
    .initialState(State())
    .renderBackend[Backend]
    .build

  def apply(group: FilterOptionGroup): VdomElement = {
    component(Props(group))
  }
}
