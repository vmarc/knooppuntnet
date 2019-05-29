// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.components.common.CssSettings.default._

import scalacss.ScalaCssReact._

object UiThick {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val thick: StyleA = style(
      display.inline,
      fontWeight.bold
    )
  }

  private case class Props(element: VdomElement)

  private val component = ScalaComponent.builder[Props]("thick")
    .render_P { props =>
      <.div(
        Styles.thick,
        props.element
      )
    }
    .build

  def apply(text: String): VdomElement = {
    this (<.span(text))
  }

  def apply(element: VdomElement): VdomElement = {
    component(Props(element))
  }

}
