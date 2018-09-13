package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.shared.NetworkType

object UiNetworkType {

  private case class Props(context: Context, networkType: NetworkType)

  private val component = ScalaComponent.builder[Props]("network-type")
    .render_P { props =>
      implicit val context: Context = props.context
      val text = if (props.networkType == NetworkType.hiking) {
        nls("Walking network", "Wandelnetwerk")
      }
      else if (props.networkType == NetworkType.bicycle) {
        nls("Cycling network", "Fietsnetwerk")
      }
      else if (props.networkType == NetworkType.horse) {
        nls("Horse network", "Ruiternetwerk")
      }
      else if (props.networkType == NetworkType.motorboat) {
        nls("Motorboat network", "Motorbootnetwerk")
      }
      else if (props.networkType == NetworkType.canoe) {
        nls("Canoe network", "Kanonetwerk")
      }
      else if (props.networkType == NetworkType.inlineSkates) {
        "Inline skates"
      }
      else {
        "?"
      }
      UiNetworkTypeAndText(props.networkType, <.span(text))
    }
    .build

  def apply(networkType: NetworkType)(implicit context: Context): VdomElement = {
    component(Props(context, networkType))
  }
}
