package kpn.client.components.common

import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.CallbackTo
import kpn.client.common.Context
import org.scalajs.dom

abstract class AbstractBackend[T] {

  private val resizeListener = (e: dom.Event) => {
    val currentWidth = pageState.ui.width
    val newWidth = PageWidth.current
    if (currentWidth != newWidth) {
      updatePageState(pageState.copy(ui = pageState.ui.copy(width = newWidth)))
    }
  }

  def installResizeListener(): Unit = {
    dom.window.addEventListener("resize", resizeListener)
  }

  def removeResizeListener(): Callback = CallbackTo {
    dom.window.removeEventListener("resize", resizeListener)
  }

  private val openSideBarCallback = CallbackTo {
    updatePageState(pageState.copy(ui = pageState.ui.copy(isSideBarOpen = true, isSideBarFilter = false)))
  }

  private val closeSideBarCallback = CallbackTo {
    updatePageState(pageState.copy(ui = pageState.ui.copy(isSideBarOpen = false, isSideBarFilter = false)))
  }

  private val openFilterSideBarCallback = CallbackTo {
    updatePageState(pageState.copy(ui = pageState.ui.copy(isSideBarOpen = true, isSideBarFilter = true)))
  }

  private val showMapCallback = CallbackTo {
    updatePageState(pageState.copy(ui = pageState.ui.copy(isMapShown = true)))
  }

  private val showListCallback = CallbackTo {
    updatePageState(pageState.copy(ui = pageState.ui.copy(isMapShown = false)))
  }

  protected def pageState: PageState[T]

  protected def updatePageState(pageState: PageState[T]): Unit

  protected def pagePropsWithContext(
    context: Context,
    hasFilter: Boolean = false,
    hasMap: Boolean = false,
    hasMapButton: Boolean = false
  ): PageProps = {
    PageProps(
      context,
      pageState.ui,
      openSideBar = openSideBarCallback,
      closeSideBar = closeSideBarCallback,
      openFilterSideBar = openFilterSideBarCallback,
      hasFilter = hasFilter,
      hasMap = hasMap,
      hasMapButton = hasMapButton,
      showList = showListCallback,
      showMap = showMapCallback
    )
  }

}
