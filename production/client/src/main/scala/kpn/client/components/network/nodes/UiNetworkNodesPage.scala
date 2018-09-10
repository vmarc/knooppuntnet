package kpn.client.components.network.nodes

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.PageTitleBuilder
import kpn.client.api.ApiClient
import kpn.client.common.NetworkPageArgs
import kpn.client.common.Nls.nls
import kpn.client.common.map.UiBigMap
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
import kpn.client.components.network.map.NetworkMap
import kpn.shared.ApiResponse
import kpn.shared.NetworkType
import kpn.shared.TimeInfo
import kpn.shared.network.NetworkNodeInfo2
import kpn.shared.network.NetworkNodesPage
import kpn.shared.tiles.ZoomLevel
import org.scalajs.dom

object UiNetworkNodesPage {

  private case class State(
    pageState: PageState[NetworkNodesPage] = PageState(),
    filterCriteria: NetworkNodeFilterCriteria = NetworkNodeFilterCriteria(),
    pageIndex: Int = 0,
    itemsPerPage: Int = 25
  )

  private class Backend(scope: BackendScope[NetworkPageArgs, State]) extends AbstractBackend[NetworkNodesPage] {

    val nodesMap = new NetworkMap()(scope.props.runNow().context)
    val nodesMapComponent = UiBigMap(nodesMap)

    protected def pageState: PageState[NetworkNodesPage] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[NetworkNodesPage]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    private def updateCriteria(criteria: NetworkNodeFilterCriteria): Unit = {
      scope.modState(s => s.copy(filterCriteria = criteria, pageIndex = 0)).runNow()
    }

    private def pageIndexChanged(pageIndex: Int): Unit = {
      scope.modState(s => s.copy(pageIndex = pageIndex)).runNow()
    }

    def render(args: NetworkPageArgs, state: State): VdomElement = {

      implicit val context = args.context

      dom.window.scrollTo(0, 0)

      val pageProps = pagePropsWithContext(args.context, hasFilter = true, hasMap = true, hasMapButton = true)

      val networkInfo = if (state.pageState.response.isDefined) {
        val ni = state.pageState.response.get.result.get.networkSummary
        NetworkSummaryCache.put(args.networkId, ni)
        NetworkNameCache.put(args.networkId, ni.name)
        PageTitleBuilder.setNetworkPageTitle(nls("Nodes", "Knooppunten"), ni.name)
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

      {
        val pages: Seq[NetworkNodesPage] = state.pageState.response.toSeq.flatMap(_.result.toSeq)
        val nodes = pages.flatMap(_.nodes)
        val routeIds = pages.flatMap(_.routeIds).map(_.toString)
        nodesMap.networkMapState.nodes = nodes
        nodesMap.networkMapState.nodeIds = nodes.map(_.id.toString)
        nodesMap.networkMapState.routeIds = routeIds
      }

      val props = NetworkNodesProps(
        timeInfo,
        args,
        pageProps,
        NetworkNameCache.get(args.networkId),
        networkInfo
      )

      new Renderer(
        nodesMap,
        nodesMapComponent,
        props,
        state,
        updateCriteria,
        pageIndexChanged
      ).render()
    }

    def retrieve(props: NetworkPageArgs): Unit = {

      scope.modState(s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(status = PageStatus.LoadStarting, isSideBarOpen = false), response = None)))
        .runNow()

      def updatePageStatus(status: PageStatus.Value): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
      }

      def updateResult(response: ApiResponse[NetworkNodesPage]): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withResponse(response))).runNow()
      }

      new Loader[ApiResponse[NetworkNodesPage]].load(
        ApiClient.networkNodes(props.networkId),
        PageStatus.LoadStarting,
        updatePageStatus,
        updateResult
      )
    }
  }

  private val component = ScalaComponent.builder[NetworkPageArgs]("network-nodes-page")
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
    nodesMap: NetworkMap,
    nodesMapComponent: VdomElement,
    props: NetworkNodesProps,
    state: State,
    updateCriteria: (NetworkNodeFilterCriteria) => Unit,
    pageIndexChanged: (Int) => Unit
  )
    extends NetworkPageRenderer(UiNetworkMenu.nodes, state.pageState.ui.status, props.networkName, props.networkSummary)(props) {

    private val nodes: Seq[NetworkNodeInfo2] = state.pageState.response.toSeq.flatMap(_.result.map(_.nodes)).flatten

    private val filterX = new NetworkNodeFilter(props.timeInfo, state.filterCriteria, updateCriteria)
    private val filteredNodes = filterX.filter(nodes)
    private val filterOptions = filterX.filterOptions(nodes)

    private val totalCount = filteredNodes.size
    private val startIndex = state.pageIndex * state.itemsPerPage
    private val endIndex = ((state.pageIndex + 1) * state.itemsPerPage).min(totalCount)

    private val pageNodes = filteredNodes.slice(startIndex, endIndex)

    nodesMap.networkMapState.nodes = filteredNodes
    nodesMap.refreshNodes()

    props.networkSummary match {

      case None =>
        nodesMap.rwnVectorTileLayer.layer.setVisible(false)
        nodesMap.rcnVectorTileLayer.layer.setVisible(false)
        nodesMap.rwnBitmapTileLayer.setVisible(false)
        nodesMap.rcnBitmapTileLayer.setVisible(false)

      case Some(networkSummary) =>

        val zoom = nodesMap.map.getView().getZoom().toInt

        if (zoom <= ZoomLevel.bitmapTileMaxZoom) {
          nodesMap.rwnBitmapTileLayer.setVisible(NetworkType.hiking == networkSummary.networkType)
          nodesMap.rcnBitmapTileLayer.setVisible(NetworkType.bicycle == networkSummary.networkType)
          nodesMap.rhnBitmapTileLayer.setVisible(NetworkType.horse == networkSummary.networkType)
          nodesMap.rmnBitmapTileLayer.setVisible(NetworkType.motorboat == networkSummary.networkType)
          nodesMap.rpnBitmapTileLayer.setVisible(NetworkType.canoe == networkSummary.networkType)
          nodesMap.rwnVectorTileLayer.layer.setVisible(false)
          nodesMap.rcnVectorTileLayer.layer.setVisible(false)
          nodesMap.rhnVectorTileLayer.layer.setVisible(false)
          nodesMap.rmnVectorTileLayer.layer.setVisible(false)
          nodesMap.rpnVectorTileLayer.layer.setVisible(false)
        }
        else if (zoom >= ZoomLevel.vectorTileMinZoom) {
          nodesMap.rwnBitmapTileLayer.setVisible(false)
          nodesMap.rcnBitmapTileLayer.setVisible(false)
          nodesMap.rhnBitmapTileLayer.setVisible(false)
          nodesMap.rmnBitmapTileLayer.setVisible(false)
          nodesMap.rpnBitmapTileLayer.setVisible(false)
          nodesMap.rwnVectorTileLayer.layer.setVisible(NetworkType.hiking == networkSummary.networkType)
          nodesMap.rcnVectorTileLayer.layer.setVisible(NetworkType.bicycle == networkSummary.networkType)
          nodesMap.rhnVectorTileLayer.layer.setVisible(NetworkType.horse == networkSummary.networkType)
          nodesMap.rmnVectorTileLayer.layer.setVisible(NetworkType.motorboat == networkSummary.networkType)
          nodesMap.rpnVectorTileLayer.layer.setVisible(NetworkType.canoe == networkSummary.networkType)
        }
    }

    protected def contents(): TagMod = {

      if (props.pageProps.ui.isMapShown) {
        state.pageState.response.whenDefined { response =>
          response.result.whenDefined { page =>
            <.div(
              <.h1(page.networkSummary.name),
              UiPageContents(
                <.div(
                  if (page.networkSummary.nodeCount > 0) {
                    nodesMapComponent
                  }
                  else {
                    <.div(nls("No nodes in network", "Geen knooppunten in netwerk"))
                  }
                )
              )
            )
          }
        }
      }
      else {
        <.div(
          <.h1(state.pageState.response.get.result.get.networkSummary.name),
          <.h2(nls("Nodes", "Knooppunten")),
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
            UiNetworkNodes(state.pageState.ui.width, state.pageState.response.get.result.get.networkType, pageNodes, startIndex),
            UiPager(
              state.itemsPerPage,
              totalCount,
              state.pageIndex,
              pageIndexChanged
            )
          )
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
