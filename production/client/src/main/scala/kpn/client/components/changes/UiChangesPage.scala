// Migrated to Angular: _changes-page.component.ts
package kpn.client.components.changes

import chandu0101.scalajs.react.components.materialui.MuiDivider
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.api.ApiClient
import kpn.client.common.BrowserLocalStorage
import kpn.client.common.ChangesPageArgs
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.User
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.common.UiPage
import kpn.client.components.common.UiPageContent
import kpn.client.components.common.UiSituationOn
import kpn.client.components.home.Loader
import kpn.client.components.menu.UiAnalysisMenu
import kpn.client.components.menu.UiSidebarFooter
import kpn.shared.ApiResponse
import kpn.shared.ChangesPage
import kpn.shared.changes.filter.ChangesParameters

object UiChangesPage {

  private case class Props(args: ChangesPageArgs)

  private class Backend(scope: BackendScope[ChangesPageArgs, ChangesState]) extends AbstractChangesPageBackend[ChangesPage] {

    protected def pageState: PageState[ChangesPage] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[ChangesPage]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    override protected def currentPage: Option[ChangesPage] = scope.state.runNow().pageState.response.flatMap(_.result)

    override protected def currentParameters: Option[ChangesParameters] = scope.state.runNow().parameters

    override protected def update(parameters: ChangesParameters): Unit = {

      User.get match {
        case None =>

          scope.modState(
            s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(status = PageStatus.NotAuthorized, isSideBarOpen = false), response = None))).runNow()

        case Some(user) =>

          scope.modState(s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(isSideBarOpen = false, status = PageStatus.UpdateStarting)))).runNow()

          def updatePageStatus(status: PageStatus.Value): Unit = {
            scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
          }

          def updateResult(response: ApiResponse[ChangesPage]): Unit = {
            scope.modState(s => s.copy(pageState = s.pageState.withResponse(response), parameters = Some(parameters))).runNow()
          }

          new Loader[ApiResponse[ChangesPage]].load(
            ApiClient.changes(parameters),
            PageStatus.UpdateStarting,
            updatePageStatus,
            updateResult
          )
      }
    }

    def render(args: ChangesPageArgs, state: ChangesState): VdomElement = {

      implicit val context: Context = args.context
      val pageProps = pagePropsWithContext(args.context, hasFilter = true)

      val content = UiPageContent(
        nls("Changes", "Wijzigingen"),
        state.pageState.ui.status,
        CallbackTo {
          <.div(
            <.h1(nls("Changes", "Wijzigingen")),
            <.br(),
            UiSituationOn(state.pageState.situationOn),
            contents()
          )
        }
      )

      def sideBar(): Seq[VdomElement] = {
        if (pageProps.ui.isSideBarFilter) {
          Seq(
            filter()
          )
        }
        else if (pageProps.hasFilter) {
          Seq(
            UiAnalysisMenu(pageProps),
            MuiDivider()(),
            filter(),
            UiSidebarFooter(pageProps)
          )
        }
        else {
          Seq(
            UiAnalysisMenu(pageProps),
            MuiDivider()(),
            UiSidebarFooter(pageProps)
          )
        }
      }

      UiPage(
        pageProps,
        sideBar(),
        content
      )
    }

    def retrieve(props: ChangesPageArgs): Unit = {

      User.get match {
        case None =>

          scope.modState(
            s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(status = PageStatus.NotAuthorized, isSideBarOpen = false), response = None))).runNow()

        case Some(user) =>

          scope.modState(
            s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(status = PageStatus.Loading, isSideBarOpen = false), response = None))).runNow()
          val parameters = ChangesParameters(itemsPerPage = BrowserLocalStorage.itemsPerPage, impact = BrowserLocalStorage.impact)

          scope.modState(s => s.copy(pageState = s.pageState.loadStarting())).runNow()

          def updatePageStatus(status: PageStatus.Value): Unit = {
            scope.modState(s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(status = status)))).runNow()
          }

          def updateResult(response: ApiResponse[ChangesPage]): Unit = {
            scope.modState(s => s.copy(pageState = s.pageState.withResponse(response), parameters = Some(parameters))).runNow()
          }

          new Loader[ApiResponse[ChangesPage]].load(
            ApiClient.changes(parameters),
            PageStatus.LoadStarting,
            updatePageStatus,
            updateResult
          )
      }
    }
  }

  private val component = ScalaComponent.builder[ChangesPageArgs]("changes")
    .initialState(ChangesState())
    .renderBackend[Backend]
    .componentWillReceiveProps { scope =>
      Callback {
        scope.backend.retrieve(scope.nextProps)
      }
    }
    .componentDidMount { scope =>
      Callback {
        scope.backend.installResizeListener()
        scope.backend.retrieve(scope.props)
      }
    }
    .componentWillUnmount { scope =>
      scope.backend.removeResizeListener()
    }
    .build

  def apply(args: ChangesPageArgs): VdomElement = component(args)

}
