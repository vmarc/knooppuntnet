// TODO migrate to Angular
package kpn.client.components.network.map

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.PageTitleBuilder
import kpn.client.api.ApiClient
import kpn.client.common.NetworkPageArgs
import kpn.client.common.Nls.nls
import kpn.client.common.map.UiBigMap
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.PageProps
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.common.UiPageContents
import kpn.client.components.home.Loader
import kpn.client.components.network.NetworkNameCache
import kpn.client.components.network.NetworkPageProps
import kpn.client.components.network.NetworkPageRenderer
import kpn.client.components.network.NetworkSummaryCache
import kpn.client.components.network.UiNetworkMenu
import kpn.shared.ApiResponse
import kpn.shared.NetworkType
import kpn.shared.network.NetworkMapPage
import kpn.shared.network.NetworkNodeInfo2
import kpn.shared.network.NetworkSummary
import kpn.shared.tiles.ZoomLevel

object UiNetworkMapPage {

  private case class State(pageState: PageState[NetworkMapPage] = PageState())

  private case class Props(
    args: NetworkPageArgs,
    pageProps: PageProps,
    networkName: Option[String],
    networkSummary: Option[NetworkSummary],
    networkMap: NetworkMap,
    networkMapComponent: VdomElement,
    nodes: Seq[NetworkNodeInfo2],
    nodeIds: Seq[String],
    routeIds: Seq[String]
  ) extends NetworkPageProps

  private class Backend(scope: BackendScope[NetworkPageArgs, State]) extends AbstractBackend[NetworkMapPage] {

    val networkMap = new NetworkMap()(scope.props.runNow().context)
    val networkMapComponent = UiBigMap(networkMap)

    protected def pageState: PageState[NetworkMapPage] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[NetworkMapPage]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    def render(args: NetworkPageArgs, state: State): VdomElement = {

      implicit val context = args.context

      val pageProps = pagePropsWithContext(args.context)

      val networkSummary = state.pageState.response match {
        case Some(apiResponse) =>
          apiResponse.result match {
            case Some(networkDetailsPage) =>
              val ni = networkDetailsPage.networkSummary
              NetworkSummaryCache.put(args.networkId, ni)
              NetworkNameCache.put(args.networkId, ni.name)
              PageTitleBuilder.setNetworkPageTitle(nls("Map", "Kaart"), ni.name)
              Some(ni)
            case _ => None
          }
        case _ => NetworkSummaryCache.get(args.networkId)
      }


      val pages: Seq[NetworkMapPage] = state.pageState.response.toSeq.flatMap(_.result.toSeq)
      val nodes = pages.flatMap(_.nodes)
      val nodeIds = pages.flatMap(_.nodeIds).map(_.toString)
      val routeIds = pages.flatMap(_.routeIds).map(_.toString)

      val props = Props(
        args,
        pageProps,
        NetworkNameCache.get(args.networkId),
        networkSummary,
        networkMap,
        networkMapComponent,
        nodes,
        nodeIds,
        routeIds
      )

      new Renderer(props, state).render()
    }

    def retrieve(props: NetworkPageArgs): Unit = {

      scope.modState(s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(status = PageStatus.LoadStarting, isSideBarOpen = false), response = None)))
        .runNow()

      def updatePageStatus(status: PageStatus.Value): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
      }

      def updateResult(response: ApiResponse[NetworkMapPage]): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withResponse(response))).runNow()
      }

      new Loader[ApiResponse[NetworkMapPage]].load(
        ApiClient.networkMap(props.networkId),
        PageStatus.LoadStarting,
        updatePageStatus,
        updateResult
      )
    }
  }

  private val component = ScalaComponent.builder[NetworkPageArgs]("network-map")
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

  private class Renderer(props: Props, state: State)
    extends NetworkPageRenderer(UiNetworkMenu.map, state.pageState.ui.status, props.networkName, props.networkSummary)(props) {

    props.networkMap.networkMapState.nodes = props.nodes
    props.networkMap.networkMapState.nodeIds = props.nodeIds.map(_.toString)
    props.networkMap.networkMapState.routeIds = props.routeIds.map(_.toString)
    props.networkMap.refreshNodes()

    props.networkSummary match {

      case None =>
        props.networkMap.rwnVectorTileLayer.layer.setVisible(false)
        props.networkMap.rcnVectorTileLayer.layer.setVisible(false)
        props.networkMap.rwnBitmapTileLayer.setVisible(false)
        props.networkMap.rcnBitmapTileLayer.setVisible(false)

      case Some(networkSummary) =>

        val zoom = props.networkMap.map.getView().getZoom().toInt

        if (zoom <= ZoomLevel.bitmapTileMaxZoom) {
          props.networkMap.rwnBitmapTileLayer.setVisible(NetworkType.hiking == networkSummary.networkType)
          props.networkMap.rcnBitmapTileLayer.setVisible(NetworkType.bicycle == networkSummary.networkType)
          props.networkMap.rhnBitmapTileLayer.setVisible(NetworkType.horse == networkSummary.networkType)
          props.networkMap.rmnBitmapTileLayer.setVisible(NetworkType.motorboat == networkSummary.networkType)
          props.networkMap.rpnBitmapTileLayer.setVisible(NetworkType.canoe == networkSummary.networkType)
          props.networkMap.rinBitmapTileLayer.setVisible(NetworkType.inlineSkates == networkSummary.networkType)
          props.networkMap.rwnVectorTileLayer.layer.setVisible(false)
          props.networkMap.rcnVectorTileLayer.layer.setVisible(false)
          props.networkMap.rhnVectorTileLayer.layer.setVisible(false)
          props.networkMap.rmnVectorTileLayer.layer.setVisible(false)
          props.networkMap.rpnVectorTileLayer.layer.setVisible(false)
          props.networkMap.rinVectorTileLayer.layer.setVisible(false)
        }
        else if (zoom >= ZoomLevel.vectorTileMinZoom) {
          props.networkMap.rwnBitmapTileLayer.setVisible(false)
          props.networkMap.rcnBitmapTileLayer.setVisible(false)
          props.networkMap.rhnBitmapTileLayer.setVisible(false)
          props.networkMap.rmnBitmapTileLayer.setVisible(false)
          props.networkMap.rpnBitmapTileLayer.setVisible(false)
          props.networkMap.rinBitmapTileLayer.setVisible(false)

          props.networkMap.rwnVectorTileLayer.layer.setVisible(NetworkType.hiking == networkSummary.networkType)
          props.networkMap.rcnVectorTileLayer.layer.setVisible(NetworkType.bicycle == networkSummary.networkType)
          props.networkMap.rhnVectorTileLayer.layer.setVisible(NetworkType.horse == networkSummary.networkType)
          props.networkMap.rmnVectorTileLayer.layer.setVisible(NetworkType.motorboat == networkSummary.networkType)
          props.networkMap.rpnVectorTileLayer.layer.setVisible(NetworkType.canoe == networkSummary.networkType)
          props.networkMap.rinVectorTileLayer.layer.setVisible(NetworkType.inlineSkates == networkSummary.networkType)
        }
    }

    protected def contents(): VdomElement = {

      val networkName: String = state.pageState.response match {
        case Some(apiResponse) =>
          apiResponse.result match {
            case Some(networkMapPage) => networkMapPage.networkSummary.name
            case _ => props.networkName.getOrElse("?")
          }
        case _ => props.networkName.getOrElse("?")
      }

      <.div(
        <.h1(networkName),
        UiPageContents(
          if (props.networkMap.networkMapState.nodes.nonEmpty) {
            props.networkMapComponent
          }
          else {
            <.div(nls("No network nodes", "Geen knooppunten in netwerk"))
          }
        )
      )
    }
  }

}
