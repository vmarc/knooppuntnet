package kpn.client.components.network.nodes.indicators

import japgolly.scalajs.react.vdom.VdomElement
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiIndicator

object ConnectionIndicator {

  def apply(connection: Boolean)(implicit context: Context): VdomElement = {

    if (connection) {
      UiIndicator(
        nls("C", "V"),
        UiIndicator.Color.BLUE,
        nls(
          "OK - Connection",
          "OK - Verbinding"
        ),
        nls(
          """This node is a connection to another network. All routes to this node have the role "connection" in the network relation.""",
          """Dit knooppunt is een verbinding naar een ander netwerk. Alle routes naar dit knooppunt hebben de rol "connection" in de netwerkrelatie."""
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
          """This node is not a connection to another network. The node would have been considered
            | as a connection to another network if all routes to this node within the network had
            | role "connection" in the network relation.""".stripMargin,
          """Dit knooppunt is geen verbinding naar een ander netwerk. Het knooppunt zou
            | beschouwd worden als een verbinding naar een ander netwerk indien alle routes naar
            | dit knooppunt de rol "connection" zouden hebben in de netwerk relatie.""".stripMargin
        )
      )
    }
  }
}
