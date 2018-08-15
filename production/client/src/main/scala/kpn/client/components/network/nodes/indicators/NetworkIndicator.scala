package kpn.client.components.network.nodes.indicators

import japgolly.scalajs.react.vdom.VdomElement
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiIndicator

object NetworkIndicator {

  def apply(ok: Boolean)(implicit context: Context): VdomElement = {
    if (ok) {
      UiIndicator(
        "N",
        UiIndicator.Color.GREEN,
        nls(
          "OK - Defined in network relation",
          "OK - Opgenomen in netwerk relatie"
        ),
        nls(
          "This node is included as a member in the network relation. This is what we expect.",
          "Dit knooppunt is opgenomen als lid in een netwerk relatie. Dit is wat we verwachten."
        )
      )
    }
    else {
      UiIndicator(
        "N",
        UiIndicator.Color.RED,
        nls(
          "NOK - Not defined in network relation",
          "NOK - Niet opgenomen in netwerk relatie"
        ),
        nls(
          "This node is not included as a member in the network relation. This is not ok. The" +
            " convention is to include each node in the network relation.",
          "Dit knooppunt is niet opgenomen als lid in een netwerk relatie. Dit is niet ok. De " +
            "conventie is om elk knooppunt op te nemen in een netwerk relatie."
        )
      )
    }
  }
}
