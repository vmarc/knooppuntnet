package kpn.client.components.node

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiNetworkTypeAndText
import kpn.shared.node.NodeOrphanRouteReference

object UiNodeOrphanRouteReferences {

  private case class Props(context: Context, references: Seq[NodeOrphanRouteReference])

  private val component = ScalaComponent.builder[Props]("node-orphan-route-references")
    .render_P { props =>
      implicit val context: Context = props.context
      if (props.references.isEmpty) {
        <.p(nls("None", "Geen"))
      }
      else {
        <.p(
          props.references.toTagMod { reference =>
            UiNetworkTypeAndText(reference.networkType, context.gotoRoute(reference.routeId, reference.routeName))
          }
        )
      }
    }
    .build

  def apply(references: Seq[NodeOrphanRouteReference])(implicit context: Context): VdomElement = {
    component(Props(context, references))
  }
}
