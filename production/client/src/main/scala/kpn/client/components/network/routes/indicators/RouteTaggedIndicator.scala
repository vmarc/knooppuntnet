package kpn.client.components.network.routes.indicators

import japgolly.scalajs.react.vdom.VdomElement
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiIndicator

object RouteTaggedIndicator {

  def apply(tagged: Boolean)(implicit context: Context): VdomElement = {
    if (tagged) {
      UiIndicator(
        "T",
        UiIndicator.Color.GREEN,
        nls(
          "OK - Extra tag present",
          "OK - Extra tag aanwezig"
        ),
        nls(
          """This route has the extra tag _"network:type=node_network"_ to indicate that
            | this route is part of a node network. This is what we expect.""".stripMargin,
          """Deze route heeft de extra tag _"network:type=node_network"_ om eenduidig aan te geven dat
            | deze route deel uitmaakt van een knooppuntnetwerk.
            | Dit is wat we verwachten.""".stripMargin
        )
      )
    }
    else {
      UiIndicator(
        "T",
        UiIndicator.Color.RED,
        nls(
          "Tagging not updated",
          "Tags niet aangepast"
        ),
        nls(
          """This route does not contain the extra tag _"network:type=node_network"_ to indicate
            | that this route is part of a node network.""".stripMargin,
          """Deze route heeft niet de extra tag _"network:type=node_network"_ om eenduidig aan te geven dat
            | deze route deel uitmaakt van een knooppuntnetwerk.""".stripMargin
        )
      )
    }
  }
}
