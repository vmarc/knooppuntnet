package kpn.client.components.route

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.components.common.UiImage
import kpn.client.components.common.UiOsmLink
import kpn.shared.route.RouteNetworkNodeInfo

object UiRouteNode {

  private case class Props(context: Context, node: RouteNetworkNodeInfo, icon: String)

  private val component = ScalaComponent.builder[Props]("route-node")
    .render_P { props =>
      implicit val context: Context = props.context
      <.p(
        UiImage(props.icon),
        " ",
        context.gotoNode(props.node.id, props.node.alternateName),
        " ",
        UiOsmLink.node(props.node.id)
      )
    }
    .build

  def apply(node: RouteNetworkNodeInfo, icon: String)(implicit context: Context): VdomElement = {
    component(Props(context, node, icon))
  }
}
