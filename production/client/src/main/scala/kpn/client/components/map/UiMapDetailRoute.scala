package kpn.client.components.map

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.api.ApiClient
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.map.vector.SelectedRoute
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.FactInfo
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.common.UiFacts
import kpn.client.components.home.Loader
import kpn.shared.ApiResponse
import kpn.shared.route.MapDetailRoute
import scalacss.ScalaCssReact._

object UiMapDetailRoute {

  private case class State(pageState: PageState[MapDetailRoute] = PageState())

  private case class Props(context: Context, selection: SelectedRoute)

  private class Backend(scope: BackendScope[Props, State]) extends AbstractBackend[MapDetailRoute] {

    protected def pageState: PageState[MapDetailRoute] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[MapDetailRoute]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    def render(props: Props, state: State): VdomElement = {
      implicit val context: Context = props.context

      if (state.pageState.response.isDefined) {
        new Renderer(props, state).render()
      }
      else {
        <.div("Route " + props.selection.name)
      }
    }

    def retrieve(props: Props): Unit = {

      scope.modState(s => s.copy(pageState = s.pageState.loadStarting())).runNow()

      def updatePageStatus(status: PageStatus.Value): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
      }

      def updateResult(response: ApiResponse[MapDetailRoute]): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withResponse(response))).runNow()
      }

      new Loader[ApiResponse[MapDetailRoute]].load(
        ApiClient.mapDetailRoute(props.selection.routeId),
        PageStatus.LoadStarting,
        updatePageStatus,
        updateResult
      )
    }
  }

  private val component = ScalaComponent.builder[Props]("route-detail")
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

  def apply(selection: SelectedRoute)(implicit context: Context): VdomElement = component(Props(context, selection))

  private class Renderer(props: Props, state: State) {

    private implicit val context: Context = props.context

    private val mapDetailRoute = state.pageState.response.get.result.get // TODO NOW make more safe + display situationOn
    private val route = mapDetailRoute.route

    def render(): VdomElement = {
      <.div(
        <.div(
          UiMapDetail.Styles.title,
          "Route " + route.summary.name
        ),
        <.div(
          UiMapDetail.Styles.moreDetails,
          context.gotoRoute(route.id, nls("more details", "meer details"))
        ),
        <.div(nls("Length", "Lengte"), ": ", route.summary.meters, " m"),
        <.div(
          UiMapDetail.Styles.subTitle,
          nls("Route last updated", "Route bewerkt")
        ),
        <.div(
          mapDetailRoute.route.lastUpdated.yyyymmddhhmm
        ),
        <.div(
          UiMapDetail.Styles.subTitle,
          nls("Relation last updated", "Relatie bewerkt")
        ),
        <.div(
          mapDetailRoute.route.summary.timestamp.yyyymmddhhmm
        ),
        TagMod.when(mapDetailRoute.references.networkReferences.nonEmpty) {
          <.div(
            <.div(
              UiMapDetail.Styles.subTitle,
              nls("Network(s)", "Netwerk(en)")
            ),
            mapDetailRoute.references.networkReferences.toTagMod { reference =>
              <.div(
                context.gotoNetworkDetails(reference.id, reference.name)
              )
            }
          )
        },
        TagMod.when(mapDetailRoute.references.isEmpty) {
          <.div(
            nls(
              "This route does not belong to a known node network (orphan).",
              "Deze route behoort niet tot een knooppuntnetwerk (wees)."
            )
          )
        },
        TagMod.when(route.facts.nonEmpty) {
          <.div(
            <.div(
              UiMapDetail.Styles.subTitle,
              nls("Facts", "Feiten")
            ),
            UiFacts(route.facts.map(FactInfo(_)))
          )
        }
      )
    }
  }

}
