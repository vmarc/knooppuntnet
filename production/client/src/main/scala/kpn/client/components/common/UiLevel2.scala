package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.components.common.CssSettings.default._

import scalacss.ScalaCssReact._

object UiLevel2 {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val level: StyleA = style(
      paddingTop(0.5.em),
      paddingBottom(0.5.em),
      margin(0.em)
    )

    val header: StyleA = style(
      fontWeight.bold,
      fontSize(1.1.em),
      paddingBottom(0.5.em)
    )

    val body: StyleA = style(
      media.maxWidth(PageWidth.SmallMaxWidth.px)(
        paddingBottom(0.5.em)
      ),
      media.minWidth((PageWidth.SmallMaxWidth + 1).px)(
        paddingLeft(20.px),
        paddingBottom(1.em)
      )
    )
  }

  private case class Props(header: TagMod, body: TagMod)

  private val component = ScalaComponent.builder[Props]("level-1")
    .render_P { props =>
      <.div(
        Styles.level,
        <.div(
          Styles.header,
          props.header
        ),
        <.div(
          Styles.body,
          props.body
        )
      )
    }
    .build

  def apply(header: TagMod, body: TagMod): VdomElement = {
    component(Props(header, body))
  }
}
