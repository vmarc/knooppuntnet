// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.components.common.CssSettings.default._

import scalacss.ScalaCssReact._

object UiDetail {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val detail: StyleA = style(
      paddingTop(0.5.em),
      paddingBottom(0.5.em)
    )
  }

  private case class Props(detail: TagMod)

  private val component = ScalaComponent.builder[Props]("detail")
    .render_P { props =>
      <.div(
        Styles.detail,
        props.detail
      )
    }
    .build

  def apply(string: String): VdomElement = {
    component(Props(<.span(string)))
  }

  def apply(detail: TagMod): VdomElement = {
    component(Props(detail))
  }
}
