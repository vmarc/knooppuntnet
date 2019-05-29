// TODO migrate to Angular
package kpn.client.components.common

import chandu0101.scalajs.react.components.materialui.DeterminateIndeterminate
import chandu0101.scalajs.react.components.materialui.Mui
import chandu0101.scalajs.react.components.materialui.MuiCircularProgress
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.components.common.CssSettings.default._

import scalacss.ScalaCssReact._

object UiSpinner {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val center: StyleA = style(
      position.absolute,
      top(50.%%),
      left(50.%%)
    )

    val box: StyleA = style(
      position.absolute,
      top(-30.px),
      left(-30.px)
    )

    val hidden: StyleA = style(
      display.none
    )
  }

  private case class Props(show: Boolean)

  private val component = ScalaComponent.builder[Props]("spinner")
    .render_P { props =>

      val styles: StyleA = if (props.show) {
        Styles.center
      }
      else {
        Styles.center + Styles.hidden
      }

      <.div(
        styles,
        <.div(
          Styles.box,
          MuiCircularProgress(
            mode = DeterminateIndeterminate.indeterminate,
            color = Mui.Styles.colors.deepOrange200
          )()
        )
      )
    }
    .build

  def apply(show: Boolean): VdomElement = {
    component(Props(show))
  }
}
