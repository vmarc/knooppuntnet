// TODO migrate to Angular
package kpn.client.components.subset.changes

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.api.ApiClient
import kpn.client.common.BrowserLocalStorage
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.SubsetPageArgs
import kpn.client.common.User
import kpn.client.components.changes.AbstractChangesPageBackend
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.common.UiPageContents
import kpn.client.components.common.UiSituationOn
import kpn.client.components.home.Loader
import kpn.client.components.subset.SubsetInfoCache
import kpn.client.components.subset.SubsetPageProps
import kpn.client.components.subset.SubsetPageRenderer
import kpn.client.components.subset.UiSubsetMenu
import kpn.shared.ApiResponse
import kpn.shared.ChangesPage
import kpn.shared.TimeInfo
import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.subset.SubsetChangesPage

object UiSubsetChangesPage {

  private case class State(
    pageState: PageState[SubsetChangesPage] = PageState(),
    parameters: Option[ChangesParameters] = None
  )

  private class Backend(scope: BackendScope[SubsetPageArgs, State]) extends AbstractChangesPageBackend[SubsetChangesPage] {

    protected def pageState: PageState[SubsetChangesPage] = scope.state.runNow().pageState

    override protected def currentPage: Option[ChangesPage] = {
      scope.state.runNow().pageState.response.flatMap { response =>
        response.result.map { subsetPage =>
          ChangesPage(subsetPage.filter, subsetPage.changes, subsetPage.totalCount)
        }
      }
    }

    override protected def currentParameters: Option[ChangesParameters] = scope.state.runNow().parameters

    protected def updatePageState(pageState: PageState[SubsetChangesPage]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    override protected def update(parameters: ChangesParameters): Unit = {

      User.get match {
        case None =>

          scope.modState(
            s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(status = PageStatus.NotAuthorized, isSideBarOpen = false), response = None))).runNow()

        case Some(user) =>

          scope.modState(s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(status = PageStatus.UpdateStarting, isSideBarOpen = false)))).runNow()

          def updatePageStatus(status: PageStatus.Value): Unit = {
            scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
          }

          def updateResult(response: ApiResponse[SubsetChangesPage]): Unit = {
            response.result.foreach { page =>
              SubsetInfoCache.put(scope.props.runNow().subset, page.subsetInfo)
            }
            scope.modState(s => s.copy(pageState = s.pageState.withResponse(response), parameters = Some(parameters))).runNow()
          }

          new Loader[ApiResponse[SubsetChangesPage]].load(
            ApiClient.subsetChanges(parameters),
            PageStatus.UpdateStarting,
            updatePageStatus,
            updateResult
          )
      }
    }

    def render(args: SubsetPageArgs, state: State): VdomElement = {

      implicit val context: Context = args.context
      val pageProps = pagePropsWithContext(args.context, hasFilter = true)

      val props = SubsetPageProps(
        TimeInfo(),
        args,
        pageProps
      )

      new Renderer(
        props,
        state,
        filter(),
        contents()
      ).render()
    }

    def retrieve(props: SubsetPageArgs): Unit = {

      User.get match {

        case None =>

          scope.modState(
            s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(status = PageStatus.NotAuthorized, isSideBarOpen = false), response = None))).runNow()

        case Some(user) =>

          val parameters = ChangesParameters(
            subset = Some(props.subset),
            itemsPerPage = BrowserLocalStorage.itemsPerPage,
            impact = BrowserLocalStorage.impact
          )

          scope.modState(s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(status = PageStatus.LoadStarting, isSideBarOpen = false), response = None)))
            .runNow()

          def updatePageStatus(status: PageStatus.Value): Unit = {
            scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
          }

          def updateResult(response: ApiResponse[SubsetChangesPage]): Unit = {
            response.result.foreach { page =>
              SubsetInfoCache.put(scope.props.runNow().subset, page.subsetInfo)
            }
            scope.modState(s => s.copy(pageState = s.pageState.withResponse(response), parameters = Some(parameters))).runNow()
          }

          new Loader[ApiResponse[SubsetChangesPage]].load(
            ApiClient.subsetChanges(parameters),
            PageStatus.LoadStarting,
            updatePageStatus,
            updateResult
          )
      }
    }
  }

  private val component = ScalaComponent.builder[SubsetPageArgs]("subset-changes")
    .initialState(State())
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

  def apply(props: SubsetPageArgs): VdomElement = {
    component(props)
  }

  private class Renderer(
    props: SubsetPageProps,
    state: State,
    changesFilter: VdomElement,
    changesContents: VdomElement
  ) extends SubsetPageRenderer(UiSubsetMenu.targetChanges, state.pageState.ui.status)(props) {

    override protected def filter(): VdomElement = changesFilter

    protected def contents(): VdomElement = {
      <.div(
        <.h1(title),
        <.h2(nls("Changes", "Wijzigingen")),
        UiPageContents(
          UiSituationOn(state.pageState.situationOn),
          changesContents
        )
      )
    }
  }

}
