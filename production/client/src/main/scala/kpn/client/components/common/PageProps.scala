// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react.Callback
import kpn.client.common.Context
import kpn.shared.ApiResponse
import kpn.shared.Timestamp

object PageState {
  def ready: PageState[Unit] = PageState(ui = PageUiState(status = PageStatus.Ready))
}

case class PageState[T](
  response: Option[ApiResponse[T]] = None,
  ui: PageUiState = PageUiState()
) {

  def withStatus(status: PageStatus.Value): PageState[T] = {
    copy(ui = ui.copy(status = status))
  }

  def loadStarting(): PageState[T] = {
    copy(ui = ui.copy(status = PageStatus.LoadStarting, isSideBarOpen = false), response = None)
  }

  def withResponse(response: ApiResponse[T]): PageState[T] = {
    val newStatus = if (response.result.isDefined) PageStatus.Ready else PageStatus.NotFound
    copy(ui = ui.copy(status = newStatus), response = Some(response))
  }

  def situationOn: Option[Timestamp] = {
    response.flatMap(_.situationOn)
  }

}

case class PageUiState(
  isSideBarOpen: Boolean = false,
  isSideBarFilter: Boolean = false,
  isMapShown: Boolean = false,
  status: PageStatus.Value = PageStatus.LoadStarting,
  width: PageWidth.Value = PageWidth.current
) {

  def sideBarOpen: PageUiState = this.copy(isSideBarOpen = true)

  def sideBarClose: PageUiState = this.copy(isSideBarOpen = false)

  def isSmall: Boolean = width == PageWidth.Small

  def isMedium: Boolean = width == PageWidth.Medium

  def isLarge: Boolean = width == PageWidth.Large

}

case class PageProps(
  context: Context,
  ui: PageUiState = PageUiState(),
  openSideBar: Callback = Callback.empty,
  closeSideBar: Callback = Callback.empty,
  openFilterSideBar: Callback = Callback.empty,
  hasFilter: Boolean = false,
  hasMap: Boolean = false,
  hasMapButton: Boolean = false,
  showList: Callback = Callback.empty,
  showMap: Callback = Callback.empty,
  languageEnglish: Callback = Callback.empty,
  languageDutch: Callback = Callback.empty
)
