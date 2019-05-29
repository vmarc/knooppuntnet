// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.components.common.CssSettings.default._

import scalacss.ScalaCssReact._

object UiItems {

  val itemPadding = 10

  object Styles extends StyleSheet.Inline {

    import dsl._

    val items: StyleA = style(
      marginTop(20.px),
      borderTopColor.lightgray,
      borderTopStyle.solid,
      borderTopWidth(1.px),
      media.maxWidth(PageWidth.SmallMaxWidth.px)(
        marginLeft((-UiPage.smallContentsMargin).px),
        marginRight((-UiPage.smallContentsMargin).px)
      )
    )

    val item: StyleA = style(
      borderBottomColor.lightgray,
      borderBottomStyle.solid,
      borderBottomWidth(1.px)
    )

    val itemRight: StyleA = style(
      display.tableCell,
      padding(itemPadding.px),
      media.minWidth((PageWidth.SmallMaxWidth + 1).px)(
        borderLeftColor.lightgray,
        borderLeftStyle.solid,
        borderLeftWidth(1.px)
      )
    )

    val itemLeft: StyleA = style(
      media.maxWidth(PageWidth.SmallMaxWidth.px)(
        display.none
      ),
      media.minWidth((PageWidth.SmallMaxWidth + 1).px)(
        display.tableCell,
        width(40.px),
        padding(itemPadding.px)
      )
    )
  }

  private case class Props(children: Seq[TagMod], startIndex: Int)

  private val component = ScalaComponent.builder[Props]("items")
    .render_P { props =>

      <.div(
        Styles.items,
        props.children.zipWithIndex.toTagMod { case (element, index) =>
          val itemNumber = props.startIndex + index + 1
          <.div(
            Styles.item,
            <.div(
              Styles.itemLeft,
              itemNumber
            ),
            <.div(
              Styles.itemRight,
              element
            )
          )
        }
      )
    }
    .build

  def apply(children: Seq[TagMod], startIndex: Int = 0): VdomElement = {
    component(Props(children, startIndex))
  }
}
