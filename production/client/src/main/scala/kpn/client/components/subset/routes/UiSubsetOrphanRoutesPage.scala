// TODO migrate to Angular
package kpn.client.components.subset.routes

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.api.ApiClient
import kpn.client.common.Nls.nls
import kpn.client.common.SubsetPageArgs
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.common.UiHappy
import kpn.client.components.common.UiItems
import kpn.client.components.common.UiPageContents
import kpn.client.components.common.UiSituationOn
import kpn.client.components.filter.UiFilter
import kpn.client.components.home.Loader
import kpn.client.components.subset.SubsetInfoCache
import kpn.client.components.subset.SubsetPageProps
import kpn.client.components.subset.SubsetPageRenderer
import kpn.client.components.subset.UiSubsetMenu
import kpn.shared.ApiResponse
import kpn.shared.RouteSummary
import kpn.shared.TimeInfo
import kpn.shared.subset.SubsetOrphanRoutesPage

object UiSubsetOrphanRoutesPage {

  private case class State(
    pageState: PageState[SubsetOrphanRoutesPage] = PageState(),
    filterCriteria: SubsetOrphanRouteFilterCriteria = SubsetOrphanRouteFilterCriteria()
  )

  private class Backend(scope: BackendScope[SubsetPageArgs, State]) extends AbstractBackend[SubsetOrphanRoutesPage] {

    protected def pageState: PageState[SubsetOrphanRoutesPage] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[SubsetOrphanRoutesPage]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    private def updateCriteria(criteria: SubsetOrphanRouteFilterCriteria): Unit = {
      scope.modState(s => s.copy(filterCriteria = criteria)).runNow()
    }

    def render(args: SubsetPageArgs, state: State): VdomElement = {

      val pageProps = pagePropsWithContext(args.context, hasFilter = true)

      val props = SubsetPageProps(
        state.pageState.response.flatMap(_.result.map(_.timeInfo)).getOrElse(TimeInfo()),
        args,
        pageProps
      )

      new Renderer(props, state, updateCriteria).render()
    }

    def retrieve(props: SubsetPageArgs): Unit = {

      scope.modState(s => s.copy(pageState = s.pageState.loadStarting())).runNow()

      def updatePageStatus(status: PageStatus.Value): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
      }

      def updateResult(response: ApiResponse[SubsetOrphanRoutesPage]): Unit = {
        response.result.foreach { page =>
          SubsetInfoCache.put(props.subset, page.subsetInfo)
        }
        scope.modState(s => s.copy(pageState = s.pageState.withResponse(response))).runNow()
      }

      new Loader[ApiResponse[SubsetOrphanRoutesPage]].load(
        ApiClient.subsetOrphanRoutes(props.subset),
        PageStatus.LoadStarting,
        updatePageStatus,
        updateResult
      )
    }
  }

  private val component = ScalaComponent.builder[SubsetPageArgs]("subset-orphan-routes-page")
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

  private class Renderer(props: SubsetPageProps, state: State, updateCriteria: (SubsetOrphanRouteFilterCriteria) => Unit)
    extends SubsetPageRenderer(UiSubsetMenu.targetOrphanRoutes, state.pageState.ui.status)(props) {

    private val routes = state.pageState.response.toSeq.flatMap(_.result.map(_.rows)).flatten

    private val filterX = new SubsetOrphanRouteFilter(props.timeInfo, state.filterCriteria, updateCriteria)
    private val filteredRoutes = filterX.filter(routes)
    private val filterOptions = filterX.filterOptions(routes)

    protected def contents(): VdomElement = {
      <.div(
        <.h1(title),
        <.h2(nls("Orphan routes", "Routewezen")),
        UiPageContents(
          UiSituationOn(state.pageState.situationOn),
          renderRoutes(filteredRoutes)
        )
      )
    }

    private def renderRoutes(routes: Seq[RouteSummary]): VdomElement = {
      if (routes.isEmpty) {
        <.div(
          <.br(),
          UiHappy(),
          " ",
          nls("No orphan routes", "Geen routewezen")
        )
      }
      else {
        UiItems(
          routes.map { route =>
            UiSubsetOrphanRouteRow(route)
          }
        )
      }
    }

    override protected def filter(): VdomElement = {
      state.pageState.response match {
        case None => <.span()
        case Some(page) =>
          new UiFilter.FilterRenderer(filterOptions).render()
      }
    }
  }

}
