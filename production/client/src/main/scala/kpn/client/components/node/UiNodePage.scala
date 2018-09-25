package kpn.client.components.node

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.PageTitleBuilder
import kpn.client.api.ApiClient
import kpn.client.common.Context
import kpn.client.common.Nls
import kpn.client.common.Nls.nls
import kpn.client.common.NodePageArgs
import kpn.client.common.NodeTagFilter
import kpn.client.common.map.UiEmbeddedMap
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.common.PageWidth
import kpn.client.components.common.UiData
import kpn.client.components.common.UiFacts
import kpn.client.components.common.UiNetworkTypeAndText
import kpn.client.components.common.UiOsmLink
import kpn.client.components.common.UiPage
import kpn.client.components.common.UiPageContent
import kpn.client.components.common.UiPageContents
import kpn.client.components.common.UiTagsTable
import kpn.client.components.home.Loader
import kpn.client.components.menu.UiAnalysisMenu
import kpn.client.components.menu.UiSidebarFooter
import kpn.shared.ApiResponse
import kpn.shared.NetworkType
import kpn.shared.node.NodePage
import org.scalajs.dom
import scalacss.ScalaCssReact._

object UiNodePage {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val red: StyleA = style(
      color.red
    )

    val networkRoutes: StyleA = style(
      margin(20.px)
    )
  }

  private case class State(pageState: PageState[NodePage] = PageState())

  private case class Props(args: NodePageArgs)

  private class Backend(scope: BackendScope[Props, State]) extends AbstractBackend[NodePage] {

    protected def pageState: PageState[NodePage] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[NodePage]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    def render(props: Props, state: State): VdomElement = {

      implicit val context: Context = props.args.context

      val contents = UiPageContent(
        nls("Node", "Knooppunt"),
        state.pageState.ui.status,
        CallbackTo {
          state.pageState.response.whenDefined { response =>
            response.result.whenDefined { page =>
              PageTitleBuilder.setTitle(page.nodeInfo.allNames)
              if (state.pageState.ui.isMapShown && !PageWidth.isVeryLarge) {
                <.div(
                  <.h1(nls("Node", "Knooppunt") + " " + page.nodeInfo.allNames),
                  UiEmbeddedMap(new NodeMap(page.nodeInfo))
                )
              }
              else {
                new Renderer(state, page).render()
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

  private val component = ScalaComponent.builder[Props]("node")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount { scope =>
      Callback {
        scope.backend.installResizeListener()
        scope.modState(s => s.copy(pageState = s.pageState.loadStarting())).runNow()

        def updatePageStatus(status: PageStatus.Value): Unit = {
          scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
        }

        def updateResult(response: ApiResponse[NodePage]): Unit = {
          scope.modState(s => s.copy(pageState = s.pageState.withResponse(response))).runNow()
        }

        new Loader[ApiResponse[NodePage]].load(
          ApiClient.node(scope.props.args.nodeId),
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

  def apply(args: NodePageArgs): VdomElement = component(Props(args))

  private class Renderer(state: State, page: NodePage)(implicit context: Context) {

    def render(): VdomElement = {
      <.div(
        <.h1(nls("Node", "Knooppunt") + " " + page.nodeInfo.allNames),
        UiPageContents(
          UiData("Summary", "Samenvatting")(
            summary()
          ),
          UiData("Situation on", "Situatie op")(
            <.div("" + state.pageState.response.flatMap(_.situationOn.map(_.yyyymmddhhmm)).getOrElse(""))
          ),
          UiData("Last updated", "Laatst bewerkt")(
            <.div(page.nodeInfo.lastUpdated.yyyymmddhhmm)
          ),
          UiData("Tags", "Labels")(
            if (page.nodeInfo.tags.isEmpty) {
              <.p(nls("None", "Geen"))
            }
            else {
              UiTagsTable(NodeTagFilter(page.nodeInfo))
            }
          ),
          UiData("Networks", "Netwerken")(
            UiNodeNetworkReferences(page.nodeInfo.tags, page.references.networkReferences)
          ),
          TagMod.when(page.references.routeReferences.nonEmpty) {
            UiData("Orphan routes", "Route wezen")(
              UiNodeOrphanRouteReferences(page.references.routeReferences)
            )
          },
          UiData("Facts", "Feiten")(
            UiFacts(page.nodeInfo.facts ++ page.references.networkReferences.flatMap(_.facts))
          ),
          TagMod.when(PageWidth.isVeryLarge) {
            UiEmbeddedMap(new NodeMap(page.nodeInfo))
          },
          UiNodeChanges(page.nodeChanges)
        )
      )
    }

    private def summary(): VdomElement = {
      <.div(
        <.p(
          UiOsmLink.josmNode(page.nodeInfo.id),
          " (",
          UiOsmLink.node(page.nodeInfo.id),
          ")"
        ),
        TagMod.unless(page.nodeInfo.active) {
          <.p(
            Styles.red,
            nls(
              "This network node is not active anymore.",
              "Dit knooppunt is niet actief meer."
            )
          )
        },
        TagMod.when(page.nodeInfo.tags.has(NetworkType.hiking.nodeTagKey)) {
          UiNetworkTypeAndText(NetworkType.hiking, <.span(nls("Hiking network node", "Wandelknooppunt")))
        },
        TagMod.when(page.nodeInfo.tags.has(NetworkType.bicycle.nodeTagKey)) {
          UiNetworkTypeAndText(NetworkType.bicycle, <.span(nls("Bicycle network node", "Fietsknooppunt")))
        },
        TagMod.when(page.nodeInfo.tags.has(NetworkType.horse.nodeTagKey)) {
          UiNetworkTypeAndText(NetworkType.horse, <.span(nls("Horse network node", "Ruiterknooppunt")))
        },
        TagMod.when(page.nodeInfo.tags.has(NetworkType.motorboat.nodeTagKey)) {
          UiNetworkTypeAndText(NetworkType.motorboat, <.span(nls("Motorboat network node", "Moterbootknooppunt")))
        },
        TagMod.when(page.nodeInfo.tags.has(NetworkType.canoe.nodeTagKey)) {
          UiNetworkTypeAndText(NetworkType.canoe, <.span(nls("Canoe network node", "Kanoknooppunt")))
        },
        TagMod.when(page.nodeInfo.tags.has(NetworkType.inlineSkates.nodeTagKey)) {
          UiNetworkTypeAndText(NetworkType.inlineSkates, <.span("Inline skates"))
        },
        page.nodeInfo.country.whenDefined { country =>
          <.p(
            Nls.country(Some(country))
          )
        },
        TagMod.when(page.nodeInfo.active && page.nodeInfo.orphan) {
          <.p(
            nls(
              "This network node does not belong to a known node network (orphan).",
              "Dit knooppunt behoort niet tot een knooppuntnetwerk (wees)."
            )
          )
        },
        TagMod.when(page.nodeInfo.ignored) {
          <.p(
            nls(
              "This network node is not included in the analysis.",
              "Dit knooppunt is niet in de analyse opgenomen."
            )
          )
        }
      )
    }
  }

}
