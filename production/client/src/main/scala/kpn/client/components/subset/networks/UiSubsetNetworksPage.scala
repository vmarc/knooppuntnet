package kpn.client.components.subset.networks

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.api.ApiClient
import kpn.client.common.Nls.nls
import kpn.client.common.SubsetPageArgs
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.GlobalStyles
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.common.PageWidth
import kpn.client.components.common.UiHappy
import kpn.client.components.common.UiItems
import kpn.client.components.common.UiMarked
import kpn.client.components.common.UiOsmLink
import kpn.client.components.common.UiPageContents
import kpn.client.components.common.UiSituationOn
import kpn.client.components.common.UiThin
import kpn.client.components.home.Loader
import kpn.client.components.network.NetworkNameCache
import kpn.client.components.subset.SubsetInfoCache
import kpn.client.components.subset.SubsetPageProps
import kpn.client.components.subset.SubsetPageRenderer
import kpn.client.components.subset.UiSubsetMenu
import kpn.shared.ApiResponse
import kpn.shared.TimeInfo
import kpn.shared.network.NetworkAttributes
import kpn.shared.subset.SubsetNetworksPage
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.StyleSheet

object UiSubsetNetworksPage {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val statusColumn: StyleA = style(
      minWidth(40.px)
    )

    val lengthColumn: StyleA = style(
      minWidth(4.em)
    )

    val timestampColumn: StyleA = style(
      minWidth(6.em)
    )

    val dialogNetworkName: StyleA = style(
      fontSize(26.px),
      paddingBottom(20.px)
    )

    val dialogLink: StyleA = style(
      paddingTop(20.px)
    )

  }

  private case class State(pageState: PageState[SubsetNetworksPage] = PageState())

  private class Backend(scope: BackendScope[SubsetPageArgs, State]) extends AbstractBackend[SubsetNetworksPage] {

    protected def pageState: PageState[SubsetNetworksPage] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[SubsetNetworksPage]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    def render(args: SubsetPageArgs, state: State): VdomElement = {

      val pageProps = pagePropsWithContext(args.context, hasMap = true, hasMapButton = true)

      val props = SubsetPageProps(
        TimeInfo(),
        args,
        pageProps
      )

      new Renderer(props, state).render()
    }

    def retrieve(props: SubsetPageArgs): Unit = {

      scope.modState(s => s.copy(pageState = s.pageState.loadStarting())).runNow()

      def updatePageStatus(status: PageStatus.Value): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
      }

      def updateResult(response: ApiResponse[SubsetNetworksPage]): Unit = {
        response.result.foreach { page =>
          SubsetInfoCache.put(props.subset, page.subsetInfo)
          page.networks.foreach { network =>
            NetworkNameCache.put(network.id, network.name)
          }
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

  private val component = ScalaComponent.builder[SubsetPageArgs]("subset-networks")
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

  def apply(args: SubsetPageArgs): VdomElement = {
    component(args)
  }

  private class Renderer(props: SubsetPageProps, state: State)
    extends SubsetPageRenderer(UiSubsetMenu.targetNetworks, state.pageState.ui.status)(props) {

    protected def contents(): TagMod = {
      state.pageState.response.whenDefined { response =>
        response.result.whenDefined { page =>
          if (props.pageProps.ui.isMapShown) {
            <.div(
              <.h1(title),
              TagMod.when(page.networkCount > 0) {
                UiSubsetNetworksMap(page)
              },
              TagMod.when(page.networkCount == 0) {
                <.div(nls("No networks", "Geen netwerken"))
              }
            )
          }
          else {
            <.div(
              <.h1(title),
              <.h2(nls("Networks", "Netwerken")),
              UiPageContents(
                UiSituationOn(state.pageState.situationOn),
                summary(page),
                TagMod.when(page.networkCount > 0 && !PageWidth.isVeryLarge)(networkList(page.networks)),
                TagMod.when(page.networkCount > 0 && PageWidth.isVeryLarge)(networkTable(page.networks))
              )
            )
          }
        }
      }
    }

    private def summary(page: SubsetNetworksPage): VdomElement = {
      if (page.networkCount == 1) {
        UiMarked(
          nls(
            s"_There is __1__ network, with a total of __${page.nodeCount}__ nodes " +
              s"and __${page.routeCount}__ routes with an overall length of __${page.km}__ km._",
            s"_Er is __1__ netwerk, met in totaal __${page.nodeCount}__ knooppunten " +
              s"en __${page.routeCount}__ routes met een totale lengte van __${page.km}__ km._"
          )
        )
      }
      else if (page.networkCount > 0) {
        UiMarked(
          nls(
            s"_There is __${page.networkCount}__ networks, with a total of __${page.nodeCount}__ nodes " +
              s"and __${page.routeCount}__ routes with an overall length of __${page.km}__ km._",
            s"_Er zijn __${page.networkCount}__ netwerken, met in totaal __${page.nodeCount}__ knooppunten " +
              s"en __${page.routeCount}__ routes met een totale lengte van __${page.km}__ km._"
          )
        )
      }
      else {
        <.i(
          nls(
            "No networks.",
            "Geen netwerken"
          )
        )
      }
    }

    private def networkList(networks: Seq[NetworkAttributes]): VdomElement = {
      UiItems(
        networks.map { network =>
          <.div(
            context.gotoNetworkDetails(network.id, network.name),
            UiThin(" " + network.percentageOkString + " "),
            TagMod.when(network.happy) {
              UiHappy()
            },
            TagMod.when(network.veryHappy) {
              UiHappy()
            },
            <.div(
              network.km + " km, " + network.nodeCount + " nodes, " + network.routeCount + " routes"
            ),
            TagMod.when(network.brokenRouteCount > 0) {
              UiThin(
                network.brokenRouteCount + " broken routes " + network.brokenRoutePercentage
              )
            },
            <.div(
              UiThin(
                <.i(
                  UiOsmLink.relation(network.id),
                  " ",
                  UiOsmLink.josmRelation(network.id)
                )
              )
            )
          )
        }
      )
    }

    private def networkTable(networks: Seq[NetworkAttributes]): VdomElement = {
      <.table(
        ^.title := "node integrity check differences",
        <.thead(
          <.tr(
            <.th(^.rowSpan := 2, ^.colSpan := 2, nls("Network", "Netwerk")),
            <.th(^.rowSpan := 2, nls("Length", "Lengte")),
            <.th(^.rowSpan := 2, nls("Nodes", "Knooppunten")),
            <.th(^.colSpan := 3, nls("Routes", "Routes")),
            <.th(^.colSpan := 3, nls("Integrity", "Integriteit")),
            <.th(^.rowSpan := 2, nls("Connections", "Verbindingen"))
          ),
          <.tr(
            <.th(""),
            <.th(^.colSpan := 2, nls("Broken", "Onderbroken")),
            <.th(^.colSpan := 2, nls("Nodes", "Knooppunten")),
            <.th("OK")
          )
        ),
        <.tbody(
          networks.toTagMod { network =>
            <.tr(
              <.td(
                context.gotoNetworkDetails(network.id, network.name)
              ),
              <.td(
                Styles.statusColumn,
                TagMod.when(network.happy) {
                  UiHappy()
                },
                TagMod.when(network.veryHappy) {
                  UiHappy()
                }
              ),
              <.td(
                Styles.lengthColumn,
                GlobalStyles.rightAlign,
                network.km + " km"
              ),
              <.td(
                GlobalStyles.rightAlign,
                network.nodeCount
              ),
              <.td(
                GlobalStyles.rightAlign,
                network.routeCount
              ),
              <.td(
                GlobalStyles.rightAlign,
                network.brokenRouteCount
              ),
              <.td(
                TagMod.when(network.brokenRouteCount > 0)(GlobalStyles.red),
                GlobalStyles.rightAlign,
                network.brokenRoutePercentage
              ),
              <.td(
                GlobalStyles.rightAlign,
                network.integrity.count
              ),
              <.td(
                GlobalStyles.rightAlign,
                network.integrity.coverage
              ),
              <.td(
                TagMod.when(!network.integrity.okRateOk)(GlobalStyles.red),
                GlobalStyles.rightAlign,
                network.integrity.okRate
              ),
              <.td(
                GlobalStyles.rightAlign,
                network.connectionCount
              )
            )
          }
        )
      )
    }
  }

}
