// TODO migrate to Angular
package kpn.client.components.network.routes

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
import kpn.shared.network.NetworkRouteInfo
import kpn.shared.network.NetworkRouteRow
import scalacss.ScalaCssReact._

object UiNetworkRoutes {

  val rowHeight = 45

  val routeNumberWidth = 30
  val routeNameSmallWidth = 60
  val routeNameLargeWidth = 100
  val analysisWidth = 150
  val roleWidth = 100
  val lengthWidth = 70
  val lastEditWidth = 300
  val editWidth = 40
  val osmWidth = 60

  object Styles extends StyleSheet.Inline {

    import dsl._

    val list: StyleA = style(
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
      position.relative,
      display.inlineBlock,
      lineHeight(rowHeight.px),
      verticalAlign.middle,
      whiteSpace.nowrap,
      textOverflow := "ellipsis",
      overflow.hidden,
      unsafeChild("a")(
        position.relative,
        zIndex(1050)
      ),
      &.hover(
        overflow.visible,
        maxWidth.initial,
        unsafeChild("a")(
          zIndex(1060),
          paddingRight(13.px),
          paddingTop(5.px),
          paddingBottom(5.px),
          backgroundColor.white
        )
      )
    )

    val routeNumberHeader: StyleA = style(
      mixin(header),
      width(routeNumberWidth.px)
    )
    val routeNumberValue: StyleA = style(
      mixin(value),
      width(routeNumberWidth.px)
    )


    val routeName: StyleA = style(
      media.maxWidth(PageWidth.SmallMaxWidth.px)(
        width(routeNameSmallWidth.px)
      ),
      media.minWidth((PageWidth.SmallMaxWidth + 1).px)(
        width(routeNameLargeWidth.px)
      )
    )
    val routeNameHeader: StyleA = style(
      mixin(header),
      mixin(routeName)
    )
    val routeNameValue: StyleA = style(
      mixin(value),
      mixin(routeName)
    )

    val analysisHeader: StyleA = style(
      mixin(header),
      width(analysisWidth.px)
    )
    val analysisValue: StyleA = style(
      mixin(value),
      width(analysisWidth.px)
    )


    val roleHeader: StyleA = style(
      mixin(header),
      width(roleWidth.px)
    )
    val roleValue: StyleA = style(
      mixin(value),
      width(roleWidth.px)
    )


    val lengthHeader: StyleA = style(
      mixin(header),
      width(lengthWidth.px)
    )
    val lengthValue: StyleA = style(
      mixin(value),
      width(lengthWidth.px)
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

  private case class Props(context: Context, pageWidth: PageWidth.Value, networkType: NetworkType, routes: Seq[NetworkRouteRow], startIndex: Int)

  private case class State(rowCount: Int = 10)

  private val component = ScalaComponent.builder[Props]("network-routes")
    .initialState(State())
    .render { scope =>
      implicit val context: Context = scope.props.context

      new Renderer(scope.props.pageWidth, scope.props.networkType, scope.props.routes, scope.props.startIndex, scope.state.rowCount).render()
    }
    .build

  def apply(pageWidth: PageWidth.Value, networkType: NetworkType, routes: Seq[NetworkRouteRow], startIndex: Int)(implicit context: Context): VdomElement =
    component(Props(context, pageWidth, networkType, routes, startIndex))

  private class Renderer(pageWidth: PageWidth.Value, networkType: NetworkType, routes: Seq[NetworkRouteRow], startIndex: Int, rowCount: Int)(implicit
    context:
    Context) {

    def render(): VdomElement = {
      if (routes.isEmpty) {
        <.div(nls("No network routes in analysis", "Geen netwerk routes in analyse"))
      }
      else {
        <.div(
          TagMod.when(!PageWidth.isSmall) {
            tableHeader()
          },
          <.div(
            UiItems.Styles.items + Styles.list,
            routes.zipWithIndex.map { case (route, index) =>
              UiNetworkRouteRow(pageWidth, index + startIndex + 1, networkType, route)
            }.toTagMod
          )
        )
      }
    }

    private def tableHeader(): VdomElement = {
      <.div(
        Styles.row,
        <.div(Styles.routeNumberHeader, "Nr"),
        <.div(Styles.analysisHeader, nls("Analysis", "Analyse")),
        <.div(Styles.routeNameHeader, "Route"),
        TagMod.when(PageWidth.isLarge || PageWidth.isVeryLarge) {
          <.div(Styles.editHeader)
        },
        TagMod.when(PageWidth.isLarge || PageWidth.isVeryLarge) {
          <.div(Styles.osmHeader)
        },
        TagMod.when(PageWidth.isVeryLarge) {
          <.div(Styles.roleHeader, nls("Role", "Rol"))
        },
        TagMod.when(PageWidth.isVeryLarge) {
          <.div(Styles.lengthHeader, nls("Length", "Lengte"))
        },
        TagMod.when(PageWidth.isLarge || PageWidth.isVeryLarge) {
          <.div(Styles.lastEditHeader, nls("Last edit", "Laatst bewerkt"))
        }
      )
    }
  }

}
