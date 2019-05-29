// TODO migrate to Angular
package kpn.client.components.network.details

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.PageTitleBuilder
import kpn.client.api.ApiClient
import kpn.client.common.NetworkPageArgs
import kpn.client.common.NetworkTagFilter
import kpn.client.common.Nls
import kpn.client.common.Nls.nls
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.common.UiData
import kpn.client.components.common.UiPageContents
import kpn.client.components.common.UiTagsTable
import kpn.client.components.home.Loader
import kpn.client.components.network.NetworkNameCache
import kpn.client.components.network.NetworkPageRenderer
import kpn.client.components.network.NetworkSummaryCache
import kpn.client.components.network.UiNetworkMenu
import kpn.shared.ApiResponse
import kpn.shared.network.NetworkAttributes
import kpn.shared.network.NetworkDetailsPage

object UiNetworkDetailsPage {

  private case class State(pageState: PageState[NetworkDetailsPage] = PageState())

  private class Backend(scope: BackendScope[NetworkPageArgs, State]) extends AbstractBackend[NetworkDetailsPage] {

    protected def pageState: PageState[NetworkDetailsPage] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[NetworkDetailsPage]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    def render(args: NetworkPageArgs, state: State): VdomElement = {

      val pageProps = pagePropsWithContext(args.context)

      val networkSummary = state.pageState.response match {
        case Some(apiResponse) =>
          apiResponse.result match {
            case Some(networkDetailsPage) =>
              val ni = networkDetailsPage.networkSummary
              NetworkSummaryCache.put(args.networkId, ni)
              NetworkNameCache.put(args.networkId, ni.name)
              PageTitleBuilder.setNetworkPageTitle("Details", ni.name)
              Some(ni)
            case _ => None
          }
        case _ => NetworkSummaryCache.get(args.networkId)
      }

      val props = NetworkDetailsProps(
        args,
        pageProps,
        NetworkNameCache.get(args.networkId),
        networkSummary
      )

      new Renderer(props, state).render()
    }

    def retrieve(props: NetworkPageArgs): Unit = {

      scope.modState(s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(status = PageStatus.LoadStarting, isSideBarOpen = false), response = None)))
        .runNow()

      def updatePageStatus(status: PageStatus.Value): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
      }

      def updateResult(response: ApiResponse[NetworkDetailsPage]): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withResponse(response))).runNow()
      }

      new Loader[ApiResponse[NetworkDetailsPage]].load(
        ApiClient.networkDetails(props.networkId),
        PageStatus.LoadStarting,
        updatePageStatus,
        updateResult
      )
    }
  }

  private val component = ScalaComponent.builder[NetworkPageArgs]("network-details")
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

  private class Renderer(props: NetworkDetailsProps, state: State)
    extends NetworkPageRenderer(UiNetworkMenu.details, state.pageState.ui.status, props.networkName, props.networkSummary)(props) {

    protected def contents(): VdomElement = {

      val networkName: String = state.pageState.response match {
        case Some(apiResponse) =>
          apiResponse.result match {
            case Some(networkDetailsPage) => networkDetailsPage.networkSummary.name
            case _ => props.networkName.getOrElse("?")
          }
        case _ => props.networkName.getOrElse("?")
      }

      val active: Boolean = state.pageState.response match {
        case Some(apiResponse) =>
          apiResponse.result match {
            case Some(networkDetailsPage) => networkDetailsPage.active
            case _ => true
          }
        case _ => true
      }

      <.div(
        <.h1(networkName),
        <.h2("Details"),
        UiPageContents(
          situationOn(),
          UiNetworkSummary(attributes),
          country(),
          lastUpdated(),
          relationLastUpdated(),
          tags(),
          <.div("active=" + active)
        )
      )
    }

    private def situationOn(): VdomElement = {
      UiData("Situation on", "Situatie op")(
        <.div("" + state.pageState.response.flatMap(_.situationOn.map(_.yyyymmddhhmm)).getOrElse(""))
      )
    }

    private def lastUpdated(): VdomElement = {
      UiData("Last updated", "Laatst bewerkt")(
        <.div(attributes.lastUpdated.yyyymmddhhmm)
      )
    }

    private def relationLastUpdated(): VdomElement = {
      UiData("Relation last updated", "Relatie bewerkt")(
        <.div(attributes.relationLastUpdated.yyyymmddhhmm)
      )
    }

    private def country(): VdomElement = {
      UiData("Country", "Land")(
        <.div(
          if (attributes.country.isDefined) {
            Nls.country(attributes.country)
          }
          else {
            nls(
              "Unsupported (not Belgium, The Netherlands or Germany)",
              "Niet België, Nederland of Duitsland"
            )
          }
        )
      )
    }

    private def tags(): VdomElement = {
      UiData("Tags", "Labels")(
        UiTagsTable(NetworkTagFilter(state.pageState.response.get.result.get.tags))
      )
    }

    private def attributes: NetworkAttributes = state.pageState.response.get.result.get.attributes
  }

}
