package kpn.client.components.network.routes.indicators

import japgolly.scalajs.react.vdom.VdomElement
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiIndicator

object InvestigateIndicator {

  def apply(investigate: Boolean)(implicit context: Context): VdomElement = {
    if (investigate) {
      UiIndicator(
        "F",
        UiIndicator.Color.RED,
        nls(
          "NOK - Investigate facts",
          "NOK - Onderzoek feiten"
        ),
        nls(
          "Something is wrong with this route.",
          "Er is iets mis met deze route."
        )
      )
    }
    else {
      UiIndicator(
        "F",
        UiIndicator.Color.GREEN,
        nls(
          "OK - No facts",
          "OK - Geen feiten"
        ),
        nls(
          "No issues found during route analysis.",
          "Geen problemen gevonden tijdens route analyse."
        )
      )
    }
  }
}
