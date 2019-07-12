// Migrated to Angular: functionality included in fact-diffs.component.ts (assume RouteBroken filtering will be done on server)
package kpn.client.components.common

import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.shared.Fact

object UiFactCommaList {

  def apply(title: String, facts: Seq[Fact], icon: Option[VdomElement])(implicit context: Context): TagMod = {

    val factNames: Seq[TagMod] = facts.filterNot(_.id == Fact.RouteBroken.id).map { fact =>
      <.span(
        nls(fact.name, fact.nlName)
      )
    }

    UiDetail(
      UiCommaList(factNames, Some(title), icon)
    )
  }
}
