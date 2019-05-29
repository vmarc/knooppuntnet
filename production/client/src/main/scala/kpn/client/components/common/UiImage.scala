// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.components.common.CssSettings.default._

import scalacss.ScalaCssReact._

object UiImage {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val image: StyleA = style(
      verticalAlign.textBottom
    )
  }

  private case class Props(name: String)

  private val component = ScalaComponent.builder[Props]("image")
    .render_P { props =>
      <.img(
        Styles.image,
        ^.src := "/assets/images/" + props.name
      )
    }
    .build

  def apply(name: String): VdomElement = {
    component(Props(name))
  }
}
