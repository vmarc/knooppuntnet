package kpn.client.components.subset

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.api.ApiClient
import kpn.client.common.SubsetPageArgs
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.home.Loader
import kpn.shared.ApiResponse
import kpn.shared.TimeInfo
import kpn.shared.subset.SubsetNetworksPage

object UiSubsetMap {

  private case class State(pageState: PageState[SubsetNetworksPage] = PageState())

  private class Backend(scope: BackendScope[SubsetPageArgs, State]) extends AbstractBackend[SubsetNetworksPage] {

    protected def pageState: PageState[SubsetNetworksPage] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[SubsetNetworksPage]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    def render(args: SubsetPageArgs, state: State): VdomElement = {

      val pageProps = pagePropsWithContext(args.context)

      val props = SubsetPageProps(
        TimeInfo(),
        args,
        pageProps
      )

      new Renderer(props, state).render()
    }

    def retrieve(props: SubsetPageArgs): Unit = {

      scope.modState(s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(status = PageStatus.LoadStarting, isSideBarOpen = false)))).runNow()

      def updatePageStatus(status: PageStatus.Value): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
      }

      def updateResult(response: ApiResponse[SubsetNetworksPage]): Unit = {
        response.result.foreach { page =>
          SubsetInfoCache.put(props.subset, page.subsetInfo)
        }
        scope.modState(s => s.copy(pageState = s.pageState.withResponse(response))).runNow()
      }

      new Loader[ApiResponse[SubsetNetworksPage]].load(
        ApiClient.subsetNetworks(props.subset),
        PageStatus.LoadStarting,
        updatePageStatus,
        updateResult
      )
    }
  }

  private val component = ScalaComponent.builder[SubsetPageArgs]("subset-map")
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

  private class Renderer(props: SubsetPageProps, state: State)
    extends SubsetPageRenderer(UiSubsetMenu.targetMap, state.pageState.ui.status)(props) {

    protected def contents(): VdomElement = {
      <.div("TODO")
    }
  }

}
