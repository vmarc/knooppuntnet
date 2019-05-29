// TODO migrate to Angular
package kpn.client.components.map

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.api.ApiClient
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.map.vector.SelectedNode
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.home.Loader
import kpn.shared.ApiResponse
import kpn.shared.NetworkType
import kpn.shared.node.MapDetailNode
import scalacss.ScalaCssReact._

object UiMapDetailNode {

  private case class State(pageState: PageState[MapDetailNode] = PageState())

  private case class Props(context: Context, networkType: NetworkType, selection: SelectedNode)

  private class Backend(scope: BackendScope[Props, State]) extends AbstractBackend[MapDetailNode] {

    protected def pageState: PageState[MapDetailNode] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[MapDetailNode]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    def render(props: Props, state: State): VdomElement = {

      implicit val context: Context = props.context

      <.div(
        <.div(
          UiMapDetail.Styles.title,
          nls("Node", "Knooppunt") + " " + props.selection.name
        ),
        // TODO NOW display something when no result in response yet
        TagMod.when(state.pageState.response.isDefined && state.pageState.response.get.result.isDefined) {
          val mapDetailNode = state.pageState.response.get.result.get
          val nodeInfo = mapDetailNode.info
          <.div(
            <.div(
              UiMapDetail.Styles.moreDetails,
              context.gotoNode(nodeInfo.id, nls("more details", "meer details"))
            ),
            <.div(
              UiMapDetail.Styles.subTitle,
              nls("Last update", "Bewerkt")
            ),
            <.div(
              nodeInfo.lastUpdated.yyyymmddhhmm
            ),
            TagMod.when(mapDetailNode.references.networkReferences.nonEmpty) {
              <.div(
                <.div(
                  UiMapDetail.Styles.subTitle,
                  nls("Network(s)", "Netwerk(en)")
                ),
                mapDetailNode.references.networkReferences.toTagMod { reference =>
                  <.div(
                    context.gotoNetworkDetails(reference.networkId, reference.networkName)
                  )
                }
              )
            },
            TagMod.when(mapDetailNode.references.routeReferences.nonEmpty) {
              <.div(
                <.div(
                  UiMapDetail.Styles.subTitle,
                  nls("Routes", "Routes")
                ),
                mapDetailNode.references.routeReferences.toTagMod { reference =>
                  <.div(
                    context.gotoRoute(reference.routeId, reference.routeName)
                  )
                }
              )
            },
            TagMod.when(mapDetailNode.references.isEmpty) {
              <.div(
                nls(
                  "This network node does not belong to a known node network (orphan).",
                  "Dit knooppunt behoort niet tot een knooppuntnetwerk (wees)."
                )
              )
            }
          )
        }
      )
    }

    def retrieve(props: Props): Unit = {
      scope.modState(s => s.copy(pageState = s.pageState.loadStarting())).runNow()

      def updatePageStatus(status: PageStatus.Value): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
      }

      def updateResult(response: ApiResponse[MapDetailNode]): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withResponse(response))).runNow()
      }

      new Loader[ApiResponse[MapDetailNode]].load(
        ApiClient.mapDetailNode(props.networkType, props.selection.nodeId),
        PageStatus.LoadStarting,
        updatePageStatus,
        updateResult
      )
    }
  }

  private val component = ScalaComponent.builder[Props]("node-detail")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount { scope =>
      Callback {
        scope.backend.retrieve(scope.props)
      }
    }.
    componentWillReceiveProps { scope =>
      Callback {
        scope.backend.retrieve(scope.nextProps)
      }
    }
    .build

  def apply(networkType: NetworkType, selection: SelectedNode)(implicit context: Context): VdomElement = {
    component(Props(context, networkType, selection))
  }
}
