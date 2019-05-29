// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.components.common.CssSettings.default._

import scalacss.ScalaCssReact._

object UiLevel4 {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val level: StyleA = UiDetail.Styles.detail

    val header: StyleA = style(
      paddingBottom(0.5.em)
    )

    val body: StyleA = style(
      media.maxWidth(PageWidth.SmallMaxWidth.px)(
        paddingLeft(5.px)
      ),
      media.minWidth((PageWidth.SmallMaxWidth + 1).px)(
        paddingLeft(20.px)
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
