package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.components.common.CssSettings.default._

import scalacss.ScalaCssReact._

object UiLevel1 {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val level: StyleA = style(
      marginTop(36.px),
      marginBottom(20.px),
      paddingTop(10.px),
      paddingLeft(10.px),
      paddingRight(10.px),
      borderTopWidth(1.px),
      borderTopStyle.solid,
      borderTopColor.lightgray
    )

    val header: StyleA = style(
      media.maxWidth(PageWidth.SmallMaxWidth.px)(
        paddingBottom(20.px),
        fontSize(1.3.em)
      ),
      media.minWidth((PageWidth.SmallMaxWidth + 1).px)(
        display.inlineBlock,
        fontSize(1.4.em)
      )
    )

    val body: StyleA = style(
      paddingTop(10.px),
      paddingBottom(20.px)
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
