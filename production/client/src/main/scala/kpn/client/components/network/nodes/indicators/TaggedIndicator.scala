package kpn.client.components.network.nodes.indicators

import japgolly.scalajs.react.vdom.VdomElement
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiIndicator

object TaggedIndicator {

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
          """This node has the extra tag _network:type=node_network_ to indicate that
            | this node is part of a node network. This is what we expect.""".stripMargin,
          """Dit knooppunt heeft de extra tag _network:type=node_network_ om eenduidig aan te geven dat
            | deze node een knooppunt in een knooppuntnetwerk is.
            | Dit is wat we verwachten.""".stripMargin
        )
      )
    }
    else {
      UiIndicator(
        "T",
        UiIndicator.Color.GRAY,
        nls(
          "Tagging not updated",
          "Tags niet aangepast"
        ),
        nls(
          """This node does not contain the extra tag _network:type=node_network_ to indicate
            | that this node is part of a node network.""".stripMargin,
          """Dit knooppunt heeft niet de extra tag _network:type=node_network_ om eenduidig aan te geven dat
            | deze node een knooppunt in een knooppuntnetwerk is.""".stripMargin
        )
      )
    }
  }
}
