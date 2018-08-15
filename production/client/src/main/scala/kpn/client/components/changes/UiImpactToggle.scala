package kpn.client.components.changes

import chandu0101.scalajs.react.components.materialui.MuiToggle
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.ReactMouseEvent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.CssSettings.default._
import scalacss.ScalaCssReact._

import scala.scalajs.js

object UiImpactToggle {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val toggleWrapper: StyleA = style(
      width(100.%%)
    )
    val toggle: StyleA = style(
      float.right,
      width(90.px)
    )
  }

  def apply(value: Boolean, onToggleFunction: (ReactMouseEvent, Boolean) => Callback)(implicit context: Context): VdomElement = {

    <.div(
      <.div(
        Styles.toggle,
        MuiToggle(
          thumbStyle = js.Dynamic.literal(
            backgroundColor = "#eee"
          ),
          thumbSwitchedStyle = js.Dynamic.literal(
            backgroundColor = "#00fd"
          ),
          trackStyle = js.Dynamic.literal(
            backgroundColor = "#ccc"
          ),
          trackSwitchedStyle = js.Dynamic.literal(
            backgroundColor = "#00f6"
          ),
          label = js.defined(nls("impacted", "impact")),
          defaultToggled = value,
          onToggle = onToggleFunction
        )()
      )
    )
  }

}
