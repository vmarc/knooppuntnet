package kpn.client.components.network.nodes.indicators

import japgolly.scalajs.react.vdom.VdomElement
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiIndicator

object ConnectionIndicator {

  def apply(ok: Boolean)(implicit context: Context): VdomElement = {

    if (ok) {
      UiIndicator(
        nls("C", "V"),
        UiIndicator.Color.BLUE,
        nls(
          "OK - Connection",
          "OK - Verbinding"
        ),
        nls(
          "This node is a connection to another network.",
          "Dit knooppunt is een verbinding naar een ander netwerk."
        )
      )
    }
    else {
      UiIndicator(
        nls("C", "V"),
        UiIndicator.Color.GRAY,
        nls(
          "OK - No connection",
          "OK - Geen verbinding"
        ),
        nls(
          "This node is not a connection to another network.",
          "Dit knooppunt is geen verbinding naar een ander netwerk."
        )
      )
    }
  }
}
