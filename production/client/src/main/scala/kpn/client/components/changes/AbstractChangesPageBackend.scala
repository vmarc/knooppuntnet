// TODO migrate to Angular
package kpn.client.components.changes

import japgolly.scalajs.react.CallbackTo
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.BrowserLocalStorage
import kpn.client.common.Context
import kpn.client.components.changes.filter.UiChangeFilter
import kpn.client.components.common.AbstractBackend
import kpn.shared.ChangesPage
import kpn.shared.changes.filter.ChangesParameters
import japgolly.scalajs.react.ReactMouseEvent

abstract class AbstractChangesPageBackend[T] extends AbstractBackend[T] {

  protected def update(parameters: ChangesParameters): Unit

  protected def currentParameters: Option[ChangesParameters]

  protected def currentPage: Option[ChangesPage]

  protected def filterChanged(year: Option[String], month: Option[String], day: Option[String], impact: Boolean): Unit = {
    BrowserLocalStorage.impact = impact
    currentParameters.foreach(p => update(p.copy(year = year, month = month, day = day, impact = impact, pageIndex = 0)))
  }

  protected def itemsPerPageChanged(itemsPerPage: Int): Unit = {
    BrowserLocalStorage.itemsPerPage = itemsPerPage
    currentParameters.foreach(p => update(p.copy(itemsPerPage = itemsPerPage, pageIndex = 0)))
  }

  protected def pageIndexChanged(pageIndex: Int): Unit = {
    currentParameters.foreach(p => update(p.copy(pageIndex = pageIndex)))
  }

  private val onToggleImpact = (event: ReactMouseEvent, newImpact: Boolean) => {
    CallbackTo {
      BrowserLocalStorage.impact = newImpact
      currentParameters.foreach(p => update(p.copy(impact = newImpact)))
    }
  }

  def contents()(implicit context: Context): VdomElement = {
    currentPage match {
      case Some(page) =>
        currentParameters match {
          case Some(parameters) =>
            new ChangesRenderer(page, parameters, page.totalCount, pageIndexChanged, onToggleImpact).render()

          case None =>
            <.div()
        }
      case None =>
        <.div()
    }
  }

  def filter()(implicit context: Context): VdomElement = {
    currentPage match {
      case Some(page) =>
        currentParameters match {
          case Some(parameters) =>
            UiChangeFilter(
              page.filter,
              filterChanged,
              parameters.itemsPerPage,
              itemsPerPageChanged
            )
          case None => <.div()
        }
      case None => <.div()
    }
  }
}
