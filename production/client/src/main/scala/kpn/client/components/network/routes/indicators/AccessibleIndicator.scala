package kpn.client.components.network.routes.indicators

import japgolly.scalajs.react.vdom.VdomElement
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiIndicator
import kpn.shared.NetworkType

object AccessibleIndicator {

  def apply(networkType: NetworkType, accessible: Boolean)(implicit context: Context): VdomElement = {

    if (Seq(NetworkType.horse, NetworkType.inlineSkates).contains(networkType)) {
      val description = nls(
        "Accessibility information is unknown for this type of route.",
        "Route toegankelijkheid gegevens zijn onbekend voor dit type route."
      )
      UiIndicator(
        "A",
        UiIndicator.Color.GRAY,
        nls(
          "OK - Accessibility unknown",
          "OK - Toegankelijkheid onbekend"
        ),
        description
      )
    }
    else if (accessible) {
      val description = networkType match {
        case NetworkType.hiking =>
          nls(
            "This route is completely accessible for hiking.",
            "Deze route is volledig toegankelijk voor wandelaars."
          )
        case NetworkType.bicycle =>
          nls(
            "This route is completely accessible for bicycle.",
            "Deze route is volledig toegankelijk voor fietsers."
          )
        case NetworkType.motorboat =>
          nls(
            "This route is completely accessible for motorboat.",
            "Deze route is volledig toegankelijk voor motorboot."
          )
        case NetworkType.canoe =>
          nls(
            "This route is completely accessible for canoe.",
            "Deze route is volledig toegankelijk voor kano."
          )
        case _ => ""
      }

      UiIndicator(
        "A",
        UiIndicator.Color.GREEN,
        nls(
          "OK - Accessible",
          "OK - Toegankelijk"
        ),
        description
      )
    }
    else {
      val description = networkType match {
        case NetworkType.hiking =>
          nls(
            "This route is not completely accessible for hiking.",
            "Deze route is niet volledig toegankelijk voor wandelaars."
          )
        case NetworkType.bicycle =>
          nls(
            "This route is not completely accessible for bicycle.",
            "Deze route is niet volledig toegankelijk voor fietsers."
          )
        case NetworkType.motorboat =>
          nls(
            "This route is not completely accessible for motorboat.",
            "Deze route is niet volledig toegankelijk voor motorboot."
          )
        case NetworkType.canoe =>
          nls(
            "This route is not completely accessible for canoe.",
            "Deze route is niet volledig toegankelijk voor kano."
          )
        case _ => ""
      }

      UiIndicator(
        "A",
        UiIndicator.Color.RED,
        nls(
          "NOK - Not Accessible",
          "NOK - Niet toegankelijk"
        ),
        description
      )
    }
  }
}
