package kpn.client.components.network.nodes.indicators

import japgolly.scalajs.react.vdom.VdomElement
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiIndicator

object RouteIndicator {

  def apply(ok: Boolean)(implicit context: Context): VdomElement = {
    if (ok) {
      UiIndicator(
        "R",
        UiIndicator.Color.GREEN,
        nls(
          "OK - Defined in route relation",
          "OK - Opgenomen in route relatie"
        ),
        nls(
          "This node is included as a member in one or more route relations.",
          "Dit knooppunt is opgenomen als lid in een of meerdere route relaties."
        )
      )
    }
    else {
      UiIndicator(
        "R",
        UiIndicator.Color.GRAY,
        nls(
          "OK - Not defined in route relation",
          "OK - Niet opgenomen in route relatie"
        ),
        nls(
          "This node is not included as a member in any route relations. This is OK as including de " +
            "node as member in the route relations is optional.",
          "Dit knooppunt is in geen enkele routerelatie opgenomen. Dit is OK want het opnemen van de knooppunten " +
            "als lid van route relaties is optioneel."
        )
      )
    }
  }
}
