package kpn.client.components.network

import chandu0101.scalajs.react.components.materialui.MuiMenuItem
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.raw.ReactElement
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.VdomNode
import kpn.client.RouteConfiguration.GotoNetworkChanges
import kpn.client.RouteConfiguration.GotoNetworkDetails
import kpn.client.RouteConfiguration.GotoNetworkFacts
import kpn.client.RouteConfiguration.GotoNetworkMap
import kpn.client.RouteConfiguration.GotoNetworkNodes
import kpn.client.RouteConfiguration.GotoNetworkRoutes
import kpn.client.common.Context
import kpn.client.common.NetworkPageArgs
import kpn.client.common.Nls.nls
import kpn.client.components.menu.UiMenuItem
import kpn.client.components.subset.UiSubsetMenu.Target
import kpn.shared.network.NetworkSummary

import scala.scalajs.js

object UiNetworkMenu {

  private case class Props(args: NetworkPageArgs, target: Target, networkSummary: Option[NetworkSummary])

  val details = Target("network")
  val map = Target("network-map")
  val facts = Target("network-facts")
  val nodes = Target("network-nodes")
  val routes = Target("network-routes")
  val changes = Target("network-changes")

  private val component = ScalaComponent.builder[Props]("network-menu")
    .render_P { props =>

      implicit val context: Context = props.args.context

      val factCount = props.networkSummary.map(_.factCount).getOrElse(0)
      val nodeCount = props.networkSummary.map(_.nodeCount).getOrElse(0)
      val routeCount = props.networkSummary.map(_.routeCount).getOrElse(0)

      val networkId = props.args.networkId

      val title: VdomNode = nls("Network", "Netwerk")

      MuiMenuItem[String](
        primaryText = title,
        initiallyOpen = true,
        primaryTogglesNestedList = true,
        nestedItems = js.Array[ReactElement](
          UiMenuItem(
            "Details",
            props.target == details,
            None,
            GotoNetworkDetails(context.lang, networkId)
          ).rawElement,
          UiMenuItem(
            nls("Map", "Kaart"),
            props.target == map,
            None,
            GotoNetworkMap(context.lang, networkId)
          ).rawElement,
          UiMenuItem(
            nls("Facts", "Feiten"),
            props.target == facts,
            Some(factCount),
            GotoNetworkFacts(context.lang, networkId)
          ).rawElement,
          UiMenuItem(
            nls("Nodes", "Knooppunten"),
            props.target == nodes,
            Some(nodeCount),
            GotoNetworkNodes(context.lang, networkId)
          ).rawElement,
          UiMenuItem(
            "Routes",
            props.target == routes,
            Some(routeCount),
            GotoNetworkRoutes(context.lang, networkId)
          ).rawElement,
          UiMenuItem(
            nls("Changes", "Wijzigingen"),
            props.target == changes,
            None,
            GotoNetworkChanges(context.lang, networkId)
          ).rawElement
        )
      )()
    }
    .build

  def apply(props: NetworkPageArgs, target: Target, networkSummary: Option[NetworkSummary]): VdomElement = {
    component(Props(props, target, networkSummary))
  }

}
