// TODO migrate to Angular
package kpn.client.components.network.routes

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.PageTitleBuilder
import kpn.client.api.ApiClient
import kpn.client.common.NetworkPageArgs
import kpn.client.common.Nls.nls
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.common.UiPageContents
import kpn.client.components.common.UiPager
import kpn.client.components.common.UiSituationOn
import kpn.client.components.filter.UiFilter
import kpn.client.components.home.Loader
import kpn.client.components.network.NetworkNameCache
import kpn.client.components.network.NetworkPageRenderer
import kpn.client.components.network.NetworkSummaryCache
import kpn.client.components.network.UiNetworkMenu
import kpn.shared.ApiResponse
import kpn.shared.TimeInfo
import kpn.shared.network.NetworkRoutesPage
import org.scalajs.dom

object UiNetworkRoutesPage {

  private case class State(
    pageState: PageState[NetworkRoutesPage] = PageState(),
    filterCriteria: NetworkRouteFilterCriteria = NetworkRouteFilterCriteria(),
    pageIndex: Int = 0,
    itemsPerPage: Int = 25
  )

  private class Backend(scope: BackendScope[NetworkPageArgs, State]) extends AbstractBackend[NetworkRoutesPage] {

    protected def pageState: PageState[NetworkRoutesPage] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[NetworkRoutesPage]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    private def updateCriteria(criteria: NetworkRouteFilterCriteria): Unit = {
      scope.modState(_.copy(filterCriteria = criteria, pageIndex = 0)).runNow()
    }

    private def pageIndexChanged(pageIndex: Int): Unit = {
      scope.modState(s => s.copy(pageIndex = pageIndex)).runNow()
    }

    def render(args: NetworkPageArgs, state: State): VdomElement = {

      dom.window.scrollTo(0, 0)

      val pageProps = pagePropsWithContext(args.context, hasFilter = true)

      val networkInfo = if (state.pageState.response.isDefined) {
        val ni = state.pageState.response.get.result.get.networkSummary
        NetworkSummaryCache.put(args.networkId, ni)
        NetworkNameCache.put(args.networkId, ni.name)
        PageTitleBuilder.setNetworkPageTitle("Routes", ni.name)
        Some(ni)
      }
      else {
        NetworkSummaryCache.get(args.networkId)
      }

      val timeInfo = if (state.pageState.response.isDefined) {
        state.pageState.response.get.result.get.timeInfo
      }
      else {
        TimeInfo()
      }

      val props = NetworkRoutesProps(
        timeInfo,
        args,
        pageProps,
        NetworkNameCache.get(args.networkId),
        networkInfo
      )

      new Renderer(props, state, updateCriteria, pageIndexChanged).render()
    }

    def retrieve(props: NetworkPageArgs): Unit = {

      scope.modState(s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(status = PageStatus.LoadStarting, isSideBarOpen = false), response = None)))
        .runNow()

      def updatePageStatus(status: PageStatus.Value): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
      }

      def updateResult(response: ApiResponse[NetworkRoutesPage]): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withResponse(response))).runNow()
      }

      new Loader[ApiResponse[NetworkRoutesPage]].load(
        ApiClient.networkRoutes(props.networkId),
        PageStatus.LoadStarting,
        updatePageStatus,
        updateResult
      )
    }
  }

  private val component = ScalaComponent.builder[NetworkPageArgs]("network-routes-page")
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

  def apply(props: NetworkPageArgs): VdomElement = component(props)

  private class Renderer(
    props: NetworkRoutesProps,
    state: State,
    updateCriteria: (NetworkRouteFilterCriteria) => Unit,
    pageIndexChanged: (Int) => Unit)
    extends NetworkPageRenderer(UiNetworkMenu.routes, state.pageState.ui.status, props.networkName, props.networkSummary)(props) {

    private val routes = state.pageState.response.toSeq.flatMap(_.result.map(_.routes)).flatten

    private val filterX = new NetworkRouteFilter(props.timeInfo, state.filterCriteria, updateCriteria)
    private val filteredRoutes = filterX.filter(routes)
    private val filterOptions = filterX.filterOptions(routes)

    private val totalCount = filteredRoutes.size
    private val startIndex = state.pageIndex * state.itemsPerPage
    private val endIndex = ((state.pageIndex + 1) * state.itemsPerPage).min(totalCount)

    private val pageRoutes = filteredRoutes.slice(startIndex, endIndex)

    protected def contents(): VdomElement = {
      <.div(
        <.h1(state.pageState.response.get.result.get.networkSummary.name),
        <.h2("Routes"),
        UiPageContents(
          UiSituationOn(state.pageState.situationOn),
          <.br(),
          TagMod.when(totalCount > state.itemsPerPage) {
            <.div(
              <.i(
                s"${startIndex + 1} ",
                nls("to", "tot"),
                s"  $endIndex ",
                nls("of", "van"),
                s" $totalCount"
              )
            )
          },
          UiPager(
            state.itemsPerPage,
            totalCount,
            state.pageIndex,
            pageIndexChanged
          ),
          UiNetworkRoutes(state.pageState.ui.width, state.pageState.response.get.result.get.networkType, pageRoutes, startIndex),
          UiPager(
            state.itemsPerPage,
            totalCount,
            state.pageIndex,
            pageIndexChanged
          )
        )
      )
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
