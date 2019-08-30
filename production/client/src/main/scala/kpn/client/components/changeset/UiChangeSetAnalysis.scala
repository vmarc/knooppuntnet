// Migrated to Angular: change-set-analysis.component.ts
package kpn.client.components.changeset

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiHappy
import kpn.client.components.common.UiInvestigate
import kpn.client.components.common.UiLine
import kpn.shared.changes.ChangeSetPage

object UiChangeSetAnalysis {

  private case class Props(context: Context, page: ChangeSetPage)

  private val component = ScalaComponent.builder[Props]("change-set-analysis")
    .render_P { props =>
      new Renderer(props.page)(props.context).render()
    }
    .build

  def apply(page: ChangeSetPage)(implicit context: Context): TagMod = component(Props(context, page))

  private class Renderer(page: ChangeSetPage)(implicit context: Context) {

    def render(): VdomElement = {
      <.div(
        happy(),
        investigate(),
        noImpact()
      )
    }

    private def happy(): TagMod = {
      result(
        page.summary.happy,
        "This changeset brought improvements.",
        "Er zijn verbeteringen in deze changeset.",
        Some(UiHappy())
      )
    }

    private def investigate(): TagMod = {
      result(
        page.summary.investigate,
        "Maybe this changeset is worth a closer look.",
        "Misschien is het de moeite om deze changeset eens na te kijken.",
        Some(UiInvestigate())
      )
    }

    private def noImpact(): TagMod = {
      result(
        page.summary.noImpact,
        "The changes do not seem to have an impact on the analysis result.",
        "De aanpassingen in deze changeset hebben geen impact op de analyse resultaten."
      )
    }

    private def result(b: Boolean, en: String, nl: String, icon: Option[VdomElement] = None): TagMod = {
      val text: VdomElement = <.i(nls(en, nl))
      TagMod.when(b) {
        <.div(
          UiLine(
            icon.whenDefined,
            text
          )
        )
      }
    }
  }

}
