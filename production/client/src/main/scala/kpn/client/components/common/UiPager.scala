// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.CssSettings.default._

import scalacss.ScalaCssReact._

object UiPager {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val pager: StyleA = style(
      paddingTop(15.px),
      paddingBottom(15.px)
    )

    val link: StyleA = style(
      paddingRight(15.px),
      cursor.pointer
    )
  }

  private case class Props(context: Context, itemsPerPage: Int, totalItemCount: Int, pageIndex: Int, pageIndexChanged: (Int) => Unit)

  private val component = ScalaComponent.builder[Props]("pager")
    .render_P { props =>
      new Renderer(props).render()
    }
    .build

  def apply(itemsPerPage: Int, totalItemCount: Int, pageIndex: Int, pageIndexChanged: (Int) => Unit)(implicit context: Context): VdomElement = {
    component(Props(context, itemsPerPage, totalItemCount, pageIndex, pageIndexChanged))
  }

  private class Renderer(props: Props) {

    private implicit val context: Context = props.context

    private val pageCount = (props.totalItemCount / props.itemsPerPage) +
      (if ((props.totalItemCount % props.itemsPerPage) > 0) 1 else 0)

    def render(): VdomElement = {
      if (pageCount <= 1) {
        <.div()
      }
      else {
        <.div(
          Styles.pager,
          TagMod.when(props.pageIndex > 0) {
            previous()
          },
          TagMod.when(props.pageIndex < pageCount - 1) {
            next()
          }
        )
      }
    }

    private def previous(): VdomElement = {
      <.a(
        Styles.link,
        ^.onClick --> CallbackTo {
          props.pageIndexChanged(props.pageIndex - 1)
        },
        "← ",
        nls("previous", "vorige")
      )
    }

    private def next(): VdomElement = {
      <.a(
        Styles.link,
        ^.onClick --> CallbackTo {
          props.pageIndexChanged(props.pageIndex + 1)
        },
        nls("next", "volgende"),
        " →"
      )
    }
  }

}
