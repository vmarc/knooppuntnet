// TODO migrate to Angular
package kpn.client.components.network.details

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiData
import kpn.client.components.common.UiImage
import kpn.client.components.common.UiNetworkType
import kpn.client.components.common.UiOsmLink
import kpn.shared.network.NetworkAttributes

object UiNetworkSummary {

  private case class Props(context: Context, attributes: NetworkAttributes)

  private val component = ScalaComponent.builder[Props]("network-summary")
    .render_P { props =>
      implicit val context: Context = props.context
      val a = props.attributes

      UiData("Summary", "Samenvatting")(
        <.div(
          <.p(s"${a.km} km"),
          <.p(s"${a.nodeCount} " + nls("nodes", "knooppunten")),
          <.p(s"${a.routeCount} routes"),
          <.p(UiNetworkType(a.networkType)),
          <.p(
            UiOsmLink.josmRelation(a.id),
            " (",
            UiOsmLink.relation(a.id),
            ")"
          ),
          TagMod.when(a.brokenRouteCount > 0) {
            <.p(
              UiImage("warning.png"),
              " ",
              nls(
                "This network contains broken (non-continuous) routes.",
                "Dit network bevat verbindingen die niet geheel aaneensluitend zijn."
              )
            )
          }
        )
      )
    }
    .build

  def apply(attributes: NetworkAttributes)(implicit context: Context): VdomElement = component(Props(context, attributes))
}
