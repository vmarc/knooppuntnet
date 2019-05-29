// TODO migrate to Angular
package kpn.client.components.network.nodes

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.PageWidth
import kpn.client.components.common.UiCommaList
import kpn.client.components.common.UiOsmLink
import kpn.client.components.network.nodes.UiNetworkNodes.Styles
import kpn.client.components.network.nodes.indicators.ConnectionIndicator
import kpn.client.components.network.nodes.indicators.IntegrityIndicator
import kpn.client.components.network.nodes.indicators.NetworkIndicator
import kpn.client.components.network.nodes.indicators.RoleConnectionIndicator
import kpn.client.components.network.nodes.indicators.RouteIndicator
import kpn.shared.NetworkType
import kpn.shared.network.NetworkNodeInfo2
import scalacss.ScalaCssReact._

object UiNetworkNodeRow {

  private case class Props(key: String, context: Context, pageWidth: PageWidth.Value, networkType: NetworkType, number: Int, nodeInfo: NetworkNodeInfo2)

  private val component = ScalaComponent.builder[Props]("row")
    .render_P { props =>
      implicit val context: Context = props.context
      new Renderer(props.networkType, props.number, props.nodeInfo).render()
    }
    .build

  def apply(key: String, pageWidth: PageWidth.Value, networkType: NetworkType, number: Int, nodeInfo: NetworkNodeInfo2)(implicit context: Context): VdomElement =
    component(Props(key, context, pageWidth, networkType, number, nodeInfo))

  private class Renderer(networkType: NetworkType, number: Int, nodeInfo: NetworkNodeInfo2)(implicit context: Context) {

    def render(): VdomElement = {
      <.div(
        Styles.itemRow,
        numberValue,
        analysis,
        title,
        routeCount,
        routes,
        lastEdit,
        edit,
        osm
      )
    }

    private def numberValue: VdomElement = {
      <.div(
        Styles.numberValue,
        number
      )
    }

    private def analysis: VdomElement = {
      <.div(
        Styles.analysisValue,
        NetworkIndicator(nodeInfo.definedInRelation, nodeInfo.connection, nodeInfo.roleConnection),
        RouteIndicator(nodeInfo.definedInRoute),
        ConnectionIndicator(nodeInfo.connection),
        RoleConnectionIndicator(nodeInfo.roleConnection),
        IntegrityIndicator(networkType, nodeInfo.integrityCheck)
      )
    }

    private def title: VdomElement = {
      <.div(
        Styles.titleValue,
        context.gotoNode(nodeInfo.id, nodeInfo.name)
      )
    }

    private def routeCount: TagMod = {
      TagMod.when(PageWidth.isLarge || PageWidth.isVeryLarge) {
        <.div(
          Styles.routeCountValue,
          <.span("" + nodeInfo.integrityCheck.map(_.expected.toString).getOrElse("-"))
        )
      }
    }

    private def routes: TagMod = {
      TagMod.when(PageWidth.isLarge || PageWidth.isVeryLarge) {
        <.div(
          Styles.routesValue,
          if (nodeInfo.routeReferences.isEmpty) {
            <.span(
              Styles.noRoutes,
              nls("no routes", "in geen enkele route")
            )
          }
          else {
            UiCommaList(
              nodeInfo.routeReferences.map { r =>
                context.gotoRoute(r.id, r.name)
              }
            )
          }
        )
      }
    }

    private def lastEdit: TagMod = {
      TagMod.when(PageWidth.isVeryLarge) {
        <.div(
          Styles.lastEditValue,
          nodeInfo.timestamp.yyyymmdd
        )
      }
    }

    private def edit: TagMod = {
      TagMod.when(PageWidth.isVeryLarge) {
        <.div(
          Styles.editValue,
          UiOsmLink.josmNode(nodeInfo.id)
        )
      }
    }

    private def osm: TagMod = {
      TagMod.when(PageWidth.isVeryLarge) {
        <.div(
          Styles.osmValue,
          UiOsmLink.node(nodeInfo.id)
        )
      }
    }
  }

}
