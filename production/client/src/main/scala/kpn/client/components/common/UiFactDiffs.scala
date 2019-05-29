// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.shared.diff.common.FactDiffs

object UiFactDiffs {

  private case class Props(context: Context, factDiffs: FactDiffs)

  private val component = ScalaComponent.builder[Props]("fact-diffs")
    .render_P { props =>
      implicit val context: Context = props.context
      <.div(
        UiFactCommaList(nls("Resolved facts", "Opgeloste feiten"), props.factDiffs.resolved.toSeq, Some(UiHappy())),
        UiFactCommaList(nls("Introduced facts", "Nieuwe feiten"), props.factDiffs.introduced.toSeq, Some(UiInvestigate())),
        UiFactCommaList(nls("Remaining facts", "Overblijvende feiten"), props.factDiffs.remaining.toSeq, None)
      )
    }
    .build

  def apply(factDiffs: FactDiffs)(implicit context: Context): VdomElement = {
    component(Props(context, factDiffs))
  }
}
