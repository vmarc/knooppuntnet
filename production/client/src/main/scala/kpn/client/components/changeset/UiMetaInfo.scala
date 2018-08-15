package kpn.client.components.changeset

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.PageWidth
import kpn.shared.data.Meta

import scalacss.ScalaCssReact._

object UiMetaInfo {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val meta: StyleA = style(
      media.maxWidth(PageWidth.SmallMaxWidth.px)(
        paddingLeft(5.px),
        borderLeftStyle.dotted,
        borderLeftWidth(1.px),
        borderLeftColor.gray,
        marginBottom(15.px)
      ),
      media.minWidth((PageWidth.SmallMaxWidth + 1).px)(
        display.inlineBlock,
        color.gray,
        unsafeChild("div")(
          display.inlineBlock,
          paddingLeft(5.px),
          paddingRight(5.px)
        )
      )
    )
  }

  private case class Props(context: Context, meta: Meta)

  private val component = ScalaComponent.builder[Props]("meta")
    .render_P { props =>
      val meta = props.meta
      <.div(
        Styles.meta,
        TagMod.when(!PageWidth.isSmall)(
          <.span(" [")
        ),
        <.div(s"v${meta.version}:${meta.changeSetId}"),
        <.div(s"${meta.timestamp.yyyymmddhhmm}"),
        TagMod.when(!PageWidth.isSmall)(
          <.span("] ")
        )
      )
    }
    .build

  def apply(meta: Meta)(implicit context: Context): VdomElement = component(Props(context, meta))

}
