// TODO migrate to Angular
package kpn.client.components.network.nodes

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.PageWidth
import kpn.client.components.common.UiItems
import kpn.shared.NetworkType
import kpn.shared.network.NetworkNodeInfo2

import scalacss.ScalaCssReact._

object UiNetworkNodes {

  val rowHeight = 45

  val numberWidth = 30
  val titleSmallWidth = 60
  val titleLargeWidth = 120
  val analysisWidth = 200
  val routeCountWidth = 20
  val routesWidth = 200
  val lastEditWidth = 120
  val editWidth = 35
  val osmWidth = 45


  object Styles extends StyleSheet.Inline {

    import dsl._

    val nodes: StyleA = style(
      marginTop(0.px)
    )

    val row: StyleA = style(
      paddingLeft(UiItems.itemPadding.px),
      paddingRight(UiItems.itemPadding.px)
    )

    val itemRow: StyleA = style(
      height(rowHeight.px),
      mixin(UiItems.Styles.item),
      mixin(row)
    )

    val header: StyleA = style(
      display.inlineBlock,
      fontWeight.bold
    )

    val value: StyleA = style(
      display.inlineBlock,
      whiteSpace.nowrap,
      overflow.hidden,
      textOverflow := "ellipsis",
      lineHeight(rowHeight.px),
      verticalAlign.middle
    )

    val numberHeader: StyleA = style(
      mixin(header),
      width(numberWidth.px)
    )
    val numberValue: StyleA = style(
      mixin(value),
      width(numberWidth.px)
    )

    val title: StyleA = style(
      media.maxWidth(PageWidth.SmallMaxWidth.px)(
        width(titleSmallWidth.px)
      ),
      media.minWidth((PageWidth.SmallMaxWidth + 1).px)(
        width(titleLargeWidth.px)
      )
    )
    val titleHeader: StyleA = style(
      mixin(header),
      mixin(title)
    )
    val titleValue: StyleA = style(
      mixin(value),
      mixin(title)
    )

    val analysisHeader: StyleA = style(
      mixin(header),
      width(analysisWidth.px)
    )
    val analysisValue: StyleA = style(
      mixin(value),
      width(analysisWidth.px)
    )

    val routesHeader: StyleA = style(
      mixin(header),
      width((routeCountWidth + routesWidth).px)
    )
    val routeCountValue: StyleA = style(
      mixin(value),
      width(routeCountWidth.px)
    )
    val routesValue: StyleA = style(
      mixin(value),
      width(routesWidth.px)
    )

    val noRoutes: StyleA = style(
      color.red
    )

    val lastEditHeader: StyleA = style(
      mixin(header),
      width(lastEditWidth.px)
    )
    val lastEditValue: StyleA = style(
      mixin(value),
      width(lastEditWidth.px)
    )

    val editHeader: StyleA = style(
      mixin(header),
      width(editWidth.px)
    )
    val editValue: StyleA = style(
      mixin(value),
      width(editWidth.px)
    )

    val osmHeader: StyleA = style(
      mixin(header),
      width(osmWidth.px)
    )
    val osmValue: StyleA = style(
      mixin(value),
      width(osmWidth.px)
    )
  }

  private case class Props(context: Context, pageWidth: PageWidth.Value, networkType: NetworkType, nodes: Seq[NetworkNodeInfo2], startIndex: Int)

  private val component = ScalaComponent.builder[Props]("network-nodes")
    .render_P { props =>
      implicit val context: Context = props.context

      if (props.nodes.isEmpty) {
        <.div(nls("No network nodes in network", "Geen netwerk knooppunten in netwerk"))
      }
      else {
        <.div(
          TagMod.when(!PageWidth.isSmall) {
            // title
            <.div(
              Styles.row,
              <.div(Styles.numberHeader, "Nr"),
              <.div(Styles.analysisHeader, nls("Analysis", "Analyse")),
              <.div(Styles.titleHeader, nls("Node", "Knooppunt")),
              TagMod.when(PageWidth.isLarge || PageWidth.isVeryLarge) {
                <.div(Styles.routesHeader, "Routes")
              },
              TagMod.when(PageWidth.isVeryLarge) {
                <.div(Styles.lastEditHeader, nls("Last edit", "Laatst bewerkt"))
              }
            )
          },
          <.div(
            UiItems.Styles.items + UiNetworkNodes.Styles.nodes,
            props.nodes.zipWithIndex.map { case (nodeInfo, index) =>
              UiNetworkNodeRow("" + nodeInfo.id, props.pageWidth, props.networkType, props.startIndex + index + 1, nodeInfo)
            }.toTagMod
          )
        )
      }
    }
    .build

  def apply(pageWidth: PageWidth.Value, networkType: NetworkType, nodes: Seq[NetworkNodeInfo2], startIndex: Int)(implicit context: Context): VdomElement = {
    component(Props(context, pageWidth, networkType, nodes, startIndex))
  }
}
