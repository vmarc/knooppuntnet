package kpn.client.components.route

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.PageTitleBuilder
import kpn.client.api.ApiClient
import kpn.client.common.Context
import kpn.client.common.RoutePageArgs
import kpn.client.common.RouteTagFilter
import kpn.client.common.map.UiEmbeddedMap
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.common.PageWidth
import kpn.client.components.common.UiData
import kpn.client.components.common.UiFacts
import kpn.client.components.common.UiPage
import kpn.client.components.common.UiPageContent
import kpn.client.components.common.UiPageContents
import kpn.client.components.common.UiTagsTable
import kpn.client.components.home.Loader
import kpn.client.components.menu.UiAnalysisMenu
import kpn.client.components.menu.UiSidebarFooter
import kpn.shared.ApiResponse
import kpn.shared.route.RouteInfoAnalysis
import kpn.shared.route.RoutePage
import org.scalajs.dom

object UiRoutePage {

  private case class State(pageState: PageState[RoutePage] = PageState())

  private case class Props(args: RoutePageArgs)

  private class Backend(scope: BackendScope[Props, State]) extends AbstractBackend[RoutePage] {

    protected def pageState: PageState[RoutePage] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[RoutePage]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    def render(props: Props, state: State): VdomElement = {

      implicit val context: Context = props.args.context

      val contents = UiPageContent(
        "Route",
        state.pageState.ui.status,
        CallbackTo {
          state.pageState.response.whenDefined { response =>
            response.result.whenDefined { page =>
              val route = page.route
              PageTitleBuilder.setTitle(route.summary.name)
              if (route.analysis.isDefined && state.pageState.ui.isMapShown && !PageWidth.isVeryLarge) {
                <.div(
                  <.h1("Route " + route.summary.name),
                  UiEmbeddedMap(new MainRouteMap(route.summary.networkType, route.analysis.get.map))
                )
              }
              else {
                new Renderer(props, state, page).render()
              }
            }
          }
        }
      )

      val pageProps = pagePropsWithContext(props.args.context, hasMap = true, hasMapButton = !PageWidth.isVeryLarge)

      UiPage(
        pageProps,
        Seq(
          UiAnalysisMenu(pageProps),
          UiSidebarFooter(pageProps)
        ),
        contents
      )
    }
  }

  private val component = ScalaComponent.builder[Props]("route")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount { scope =>
      Callback {
        scope.backend.installResizeListener()

        scope.modState(s => s.copy(pageState = s.pageState.loadStarting())).runNow()

        def updatePageStatus(status: PageStatus.Value): Unit = {
          scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
        }

        def updateResult(response: ApiResponse[RoutePage]): Unit = {
          scope.modState(s => s.copy(pageState = s.pageState.withResponse(response))).runNow()
        }

        new Loader[ApiResponse[RoutePage]].load(
          ApiClient.route(scope.props.args.routeId),
          PageStatus.LoadStarting,
          updatePageStatus,
          updateResult
        )
      }
    }
    .componentWillUnmount { scope =>
      scope.backend.removeResizeListener()
    }
    .build

  def apply(args: RoutePageArgs): VdomElement = component(Props(args))

  private class Renderer(props: Props, state: State, page: RoutePage) {

    private implicit val context: Context = props.args.context

    private val route = page.route

    def render(): VdomElement = {
      <.div(
        <.h1("Route " + route.summary.name),
        UiPageContents(
          UiData("Summary", "Samenvatting")(
            UiRouteSummary(page.route)
          ),
          UiData("Situation on", "Situatie op")(
            <.div("" + state.pageState.response.flatMap(_.situationOn.map(_.yyyymmddhhmm)).getOrElse(""))
          ),
          UiData("Last updated", "Laatst bewerkt")(
            <.div(page.route.lastUpdated.yyyymmddhhmm)
          ),
          UiData("Relation last updated", "Relatie bewerkt")(
            <.div(page.route.summary.timestamp.yyyymmddhhmm)
          ),
          TagMod.when(!route.ignored) {
            UiData("Network", "Netwerk")(
              UiNetworkReferences(page.references.networkReferences)
            )
          },
          TagMod.when(page.route.analysis.isDefined) {
            UiData("Start node", "Start knooppunt")(
              startNodes(page.route.analysis.get)
            )
          },
          TagMod.when(page.route.analysis.isDefined) {
            UiData("End node", "Eind knooppunt")(
              endNodes(page.route.analysis.get)
            )
          },
          TagMod.when(page.route.analysis.isDefined && page.route.analysis.get.map.redundantNodes.nonEmpty) {
            UiData("Redundant nodes", "Bijkomende knooppunten")(
              redundantNodes(page.route.analysis.get)
            )
          },
          TagMod.when(!route.ignored) {
            UiData("Number of ways", "Aantal wegen")(
              wayCount()
            )
          },
          UiData("Tags", "Labels")(
            <.p(
              UiTagsTable(RouteTagFilter(page.route))
            )
          ),
          TagMod.when(route.analysis.isDefined && (PageWidth.isLarge || PageWidth.isVeryLarge)) {
            UiData("Structure", "Structuur")(
              UiRouteStructure(route.analysis.get.structureStrings)
            )
          },
          UiData("Facts", "Feiten")(
            UiFacts(route.facts, Some(route))
          ),
          TagMod.when(PageWidth.isLarge || PageWidth.isVeryLarge) {
            UiRouteMembers(page)
          },
          TagMod.when(page.routeChangeInfos.changes.nonEmpty) {
            UiRouteChanges(page.routeChangeInfos)
          },
          TagMod.when(route.analysis.isDefined && PageWidth.isVeryLarge) {
            UiEmbeddedMap(new MainRouteMap(route.summary.networkType, route.analysis.get.map))
          }
        )
      )
    }

    private def startNodes(analysis: RouteInfoAnalysis): VdomElement = {
      <.div(
        if (analysis.startNodes.isEmpty) {
          <.p("?")
        } else {
          analysis.startNodes.toTagMod { node =>
            UiRouteNode(node, "marker-icon-green-small.png")
          }
        },
        analysis.startTentacleNodes.toTagMod { node =>
          UiRouteNode(node, "marker-icon-orange-small.png")
        }
      )
    }

    private def endNodes(analysis: RouteInfoAnalysis): VdomElement = {
      <.div(
        if (analysis.endNodes.isEmpty) {
          <.p("?")
        } else {
          analysis.endNodes.toTagMod { node =>
            UiRouteNode(node, "marker-icon-red-small.png")
          }
        },
        analysis.endTentacleNodes.toTagMod { node =>
          UiRouteNode(node, "marker-icon-purple-small.png")
        }
      )
    }

    private def redundantNodes(analysis: RouteInfoAnalysis): VdomElement = {
      <.div(
        analysis.map.redundantNodes.toTagMod { node =>
          UiRouteNode(node, "marker-icon-yellow-small.png")
        }
      )
    }

    private def wayCount(): VdomElement = {
      <.p(route.summary.wayCount)
    }
  }

}
