package kpn.client.components.network.routes.indicators

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
          "This route is a connection to another network.",
          "Deze route is een verbinding naar een ander netwerk."
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
          "This route is not a connection to another network.",
          "Deze route is geen verbinding naar een ander netwerk."
        )
      )
    }
  }
}
