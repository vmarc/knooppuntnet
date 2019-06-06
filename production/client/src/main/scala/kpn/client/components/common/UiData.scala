// Migrated to Angular: data.component.ts
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.CssSettings.default._

import scalacss.ScalaCssReact._

object UiData {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val data: StyleA = style(
      media.maxWidth(PageWidth.SmallMaxWidth.px)(
        paddingBottom(20.px)
      ),
      media.minWidth((PageWidth.SmallMaxWidth + 1).px)(
        marginBottom(35.px)
      )
    )

    val title: StyleA = style(
      media.maxWidth(PageWidth.SmallMaxWidth.px)(
        overflow.hidden,
        textOverflow := "ellipsis",
        whiteSpace.nowrap,
        padding(5.px),
        left(10.px),
        right(10.px),
        borderTopWidth(1.px),
        borderTopStyle.solid,
        borderTopColor.lightgray,
        borderBottomWidth(1.px),
        borderBottomStyle.solid,
        borderBottomColor.lightgray
      ),
      media.minWidth((PageWidth.SmallMaxWidth + 1).px)(
        float.left,
        width(160.px),
        overflow.hidden,
        clear.left,
        textOverflow := "ellipsis",
        whiteSpace.nowrap,
        color.black
      )
    )

    val body: StyleA = style(
      media.maxWidth(PageWidth.SmallMaxWidth.px)(
        marginTop(15.px),
        marginLeft(15.px),
        marginRight(15.px),
        marginBottom(15.px)
      ),
      media.minWidth((PageWidth.SmallMaxWidth + 1).px)(
        marginLeft(180.px)
      )
    )
  }

  private case class Props(context: Context, en: String, nl: String, anchor: Option[String], child: VdomElement)

  private val component = ScalaComponent.builder[Props]("data")
    .render_P { props =>

      implicit val context: Context = props.context

      <.div(
        Styles.data,
        <.div(
          Styles.title,
          TagMod.when(props.anchor.isDefined) {
            <.a(^.id := props.anchor.get)
          },
          nls(props.en, props.nl)
        ),
        <.div(
          Styles.body,
          props.child
        )
      )
    }
    .build

  def apply(en: String, nl: String, anchor: Option[String] = None)(child: VdomElement)(implicit context: Context): VdomElement = {
    component(Props(context, en, nl, anchor, child))
  }
}
