// Migrated to Angular: kpn-thin css style
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.components.common.CssSettings.default._

import scalacss.ScalaCssReact._

object UiThin {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val thin: StyleA = style(
      display.inline,
      color.gray,
      unsafeChild("a")(
        color(rgba(0, 0, 255, 0.7)
        )
      )
    )
  }

  private case class Props(element: VdomElement)

  private val component = ScalaComponent.builder[Props]("thin")
    .render_P { props =>
      <.div(
        Styles.thin,
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
