// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.components.common.CssSettings.default._

import scalacss.ScalaCssReact._

object UiNote {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val note: StyleA = style(
      fontStyle.italic,
      media.maxWidth(PageWidth.SmallMaxWidth.px)(
        paddingTop(10.px)
      ),
      media.minWidth((PageWidth.SmallMaxWidth + 1).px)(
        paddingTop(10.px)
      )
    )
  }

  private case class Props(text: String)

  private val component = ScalaComponent.builder[Props]("note")
    .render_P { props =>
      <.div(
        Styles.note,
        UiMarked(props.text)
      )
    }
    .build

  def apply(text: String): VdomElement = component(Props(text))

}
