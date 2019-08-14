// TODO migrate to Angular
package kpn.client.components.changes

import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.CallbackTo
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Month
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiItems
import kpn.client.components.common.UiPageContents
import kpn.client.components.common.UiPager
import kpn.shared.ChangesPage
import kpn.shared.changes.filter.ChangesParameters
import japgolly.scalajs.react.ReactMouseEvent

class ChangesRenderer(
  page: ChangesPage,
  parameters: ChangesParameters,
  totalItemCount: Int,
  pageIndexChanged: (Int) => Unit,
  onToggleImpact: (ReactMouseEvent, Boolean) => Callback
)(implicit context: Context) {

  def render(): VdomElement = {
    UiPageContents(
      impact(),
      periods(),
      fromTo(),
      UiPager(
        parameters.itemsPerPage,
        page.changeCount,
        parameters.pageIndex,
        pageIndexChanged
      ),
      changes(),
      UiPager(
        parameters.itemsPerPage,
        page.changeCount,
        parameters.pageIndex,
        pageIndexChanged
      )
    )
  }

  private def fromTo(): VdomElement = {
    val from = parameters.itemsPerPage * parameters.pageIndex + 1
    val to = totalItemCount.min(parameters.itemsPerPage * (parameters.pageIndex + 1))
    if (totalItemCount < 2) {
      <.div()
    }
    else {
      <.div(
        <.i(
          nls("changes", "wijzigingen"),
          s" $from ",
          nls("to", "tot"),
          s"  $to ",
          nls("of", "van"),
          s" $totalItemCount"
        )
      )
    }
  }

  private def impact(): VdomElement = {
    UiImpactToggle(parameters.impact, onToggleImpact)
  }

  private def periods(): VdomElement = {
    page.filter.periods.find(_.selected) match {
      case Some(selectedYear) =>

        selectedYear.periods.find(_.selected) match {
          case Some(selectedMonth) =>

            selectedMonth.periods.find(_.selected) match {
              case Some(selectedDay) =>
                <.div(s"${selectedDay.name} ${Month.name(selectedMonth.name)} ${selectedYear.name}")
              case None =>
                <.div(s"${Month.name(selectedMonth.name)} ${selectedYear.name}")
            }

          case None =>
            <.div(selectedYear.name)
        }

      case None =>
        <.div()
    }
  }

  private def changes(): VdomElement = {
    val items = page.changes.map { changeSetSummary =>
      UiChangeSetSummary(context, changeSetSummary)
    }
    val startIndex = parameters.pageIndex * parameters.itemsPerPage
    UiItems(items, startIndex)
  }
}
