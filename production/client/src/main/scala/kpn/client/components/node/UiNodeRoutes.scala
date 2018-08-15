package kpn.client.components.node

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiNetworkTypeAndText
import kpn.shared.common.Reference

object UiNodeRoutes {

  private case class Props(context: Context, references: Seq[Reference])

  private val component = ScalaComponent.builder[Props]("node-routes")
    .render_P { props =>
      implicit val context: Context = props.context
      if (props.references.isEmpty) {
        <.p(nls("None", "Geen"))
      }
      else {
        <.p(
          props.references.toTagMod { reference =>
            UiNetworkTypeAndText(reference.networkType, context.gotoRoute(reference.id, reference.name))
          }
        )
      }
    }
    .build

  def apply(references: Seq[Reference])(implicit context: Context): VdomElement = {
    component(Props(context, references))
  }
}
