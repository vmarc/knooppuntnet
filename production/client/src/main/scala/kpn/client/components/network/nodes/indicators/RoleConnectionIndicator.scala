// Migrated to Angular: role-connection-indicator.component.ts and role-connection-indicator-dialog.component.ts
package kpn.client.components.network.nodes.indicators

import japgolly.scalajs.react.vdom.VdomElement
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiIndicator

object RoleConnectionIndicator {

  def apply(nodeConnection: Boolean)(implicit context: Context): VdomElement = {

    if (nodeConnection) {
      UiIndicator(
        nls("C", "V"),
        UiIndicator.Color.BLUE,
        nls(
          "OK - Connection",
          "OK - Verbinding"
        ),
        nls(
          """This node is a connection to another network. This node has role "connection" in the network relation.""",
          """Dit knooppunt is een verbinding naar een ander netwerk. Het knooppunt heeft rol "connection" in de netwerkrelatie."""
        )
      )
    }
    else {
      UiIndicator(
        nls("C", "V"),
        UiIndicator.Color.GRAY,
        nls(
          "OK - No connection role",
          "OK - Geen verbinding rol"
        ),
        nls(
          """This node does not have role "connection" in het network relation.""",
          """Dit knooppunt heeft niet de rol "connection" in de netwerk relatie."""
        )
      )
    }
  }

}
