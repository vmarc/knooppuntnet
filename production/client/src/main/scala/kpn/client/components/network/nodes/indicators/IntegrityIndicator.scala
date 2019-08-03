// Migrated to Angular: integrity-indicator.component.ts and integrity-indicator-dialog.component.ts
package kpn.client.components.network.nodes.indicators

import japgolly.scalajs.react.vdom.VdomElement
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiIndicator
import kpn.shared.NetworkType
import kpn.shared.NodeIntegrityCheck

object IntegrityIndicator {

  def apply(networkType: NetworkType, integrityCheck: Option[NodeIntegrityCheck])(implicit context: Context): VdomElement = {
    new Renderer(networkType, integrityCheck).render()
  }

  private class Renderer(networkType: NetworkType, integrityCheckOption: Option[NodeIntegrityCheck])(implicit context: Context) {

    private def expected = integrityCheckOption.get.expected

    private def actual = integrityCheckOption.get.actual

    private val tag = s"""__"expected \\_ $networkType \\_ route \\_ relations"__"""

    def render(): VdomElement = {
      integrityCheckOption match {
        case None => integrityCheckMissing()
        case Some(integrityCheck) =>
          if (integrityCheck.failed) {
            integrityCheckNok()
          }
          else {
            integrityCheckOk()
          }
      }
    }

    private def integrityCheckMissing(): VdomElement = {
      UiIndicator(
        "E",
        UiIndicator.Color.GRAY,
        nls(
          "OK - expected route count missing",
          "OK - verwacht aantal routes ontbreekt"
        ),
        nls(
          s"""This network node does not have an $tag tag. """ +
            "This is OK because the use of this tag is optional. ",
          s"""Dit knooppunt heeft geen $tag tag. Dit is OK omdat het gebruik van """ +
            "deze tag optioneel is."
        )
      )
    }

    private def integrityCheckOk(): VdomElement = {
      UiIndicator(
        "E",
        UiIndicator.Color.GREEN,
        nls(
          "OK - expected route count",
          "OK - verwacht aantal routes"
        ),
        nls(
          s"The number of routes found in this network node ($actual) does match the expected number of " +
            s"""routes ($expected) as defined in the $tag tag on this node.""" +
            " This is what we expect.",
          s"Het aantal routes gevonden in dit knooppunt ($actual) komt overeen met het verwachte aantal " +
            s"""routes ($expected) zoals gedefinieerd in de $tag tag op het knooppunt.""" +
            " Dit is wat we verwachten."
        )
      )
    }

    private def integrityCheckNok(): VdomElement = {
      UiIndicator(
        "E",
        UiIndicator.Color.RED,
        nls(
          "NOK - unexpected route count",
          "NOK - onverwacht aantal routes"
        ),
        nls(
          s"The number of routes found in this network node ($actual) does not match the expected number of " +
            s"""routes ($expected) as defined in the $tag tag on this node.""",
          s"Het aantal routes gevonden in dit knooppunt ($actual) komt niet overeen met het verwachte aantal " +
            s"""routes ($expected) zoals gedefinieerd in de $tag tag op het knooppunt."""
        )
      )
    }
  }
}
