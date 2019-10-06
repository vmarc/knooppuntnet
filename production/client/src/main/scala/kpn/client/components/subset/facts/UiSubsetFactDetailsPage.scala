// TODO migrate to Angular
package kpn.client.components.subset.facts

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.api.ApiClient
import kpn.client.common.Nls.nls
import kpn.client.common.Nls.nlsNL
import kpn.client.common.SubsetPageArgs
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.common.UiCommaList
import kpn.client.components.common.UiItems
import kpn.client.components.common.UiOsmLink
import kpn.client.components.common.UiPageContents
import kpn.client.components.common.UiThin
import kpn.client.components.facts.UiFactDescription
import kpn.client.components.home.Loader
import kpn.client.components.subset.SubsetInfoCache
import kpn.client.components.subset.SubsetPageProps
import kpn.client.components.subset.SubsetPageRenderer
import kpn.client.components.subset.UiSubsetMenu
import kpn.shared.ApiResponse
import kpn.shared.Fact
import kpn.shared.Subset
import kpn.shared.TimeInfo
import kpn.shared.subset.SubsetFactDetailsPage

object UiSubsetFactDetailsPage {

  private case class State(pageState: PageState[SubsetFactDetailsPage] = PageState())

  private class Backend(scope: BackendScope[SubsetPageArgs, State]) extends AbstractBackend[SubsetFactDetailsPage] {

    protected def pageState: PageState[SubsetFactDetailsPage] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[SubsetFactDetailsPage]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    def render(args: SubsetPageArgs, state: State): VdomElement = {

      val pageProps = pagePropsWithContext(args.context)

      val props = SubsetPageProps(
        TimeInfo(),
        args,
        pageProps,
        args.fact
      )

      new Renderer(props, state).render()
    }

    def retrieve(props: SubsetPageArgs): Unit = {

      scope.modState(s => s.copy(pageState = s.pageState.loadStarting())).runNow()

      def updatePageStatus(status: PageStatus.Value): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
      }

      def updateResult(response: ApiResponse[SubsetFactDetailsPage]): Unit = {
        response.result.foreach { page =>
          SubsetInfoCache.put(props.subset, page.subsetInfo)
        }
        scope.modState(s => s.copy(pageState = s.pageState.withResponse(response))).runNow()
      }

      new Loader[ApiResponse[SubsetFactDetailsPage]].load(
        ApiClient.subsetFactDetails(props.subset, props.fact.get),
        PageStatus.LoadStarting,
        updatePageStatus,
        updateResult
      )
    }
  }

  private val component = ScalaComponent.builder[SubsetPageArgs]("subset-fact")
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
    extends SubsetPageRenderer(UiSubsetMenu.targetFacts, state.pageState.ui.status)(props) {

    def page: SubsetFactDetailsPage = state.pageState.response.get.result.get

    private def factType: String = page.fact match {
      case Fact.NodeMemberMissing => nls("node", "knooppunt")
      case Fact.IntegrityCheckFailed => nls("node", "knooppunt")
      case Fact.NetworkExtraMemberNode => nls("node", "knoop")
      case Fact.NetworkExtraMemberWay => nls("way", "weg")
      case Fact.NetworkExtraMemberRelation => nls("relation", "relatie")
      case _ => "route"
    }

    private def factTypePlural: String = page.fact match {
      case Fact.NodeMemberMissing => nls("nodes", "knooppunten")
      case Fact.IntegrityCheckFailed => nls("nodes", "knooppunten")
      case Fact.NetworkExtraMemberNode => nls("nodes", "knopen")
      case Fact.NetworkExtraMemberWay => nls("ways", "wegen")
      case Fact.NetworkExtraMemberRelation => nls("relations", "relaties")
      case _ => "routes"
    }

    protected def contents(): VdomElement = {
      <.div(
        <.h1(title),
        <.h2(nls(page.fact.name, page.fact.nlName)),
        UiPageContents(
          state.pageState.response match {
            case None =>
              <.i("No facts")
            case Some(page) =>
              facts()
          }
        )
      )
    }

    private def facts(): VdomElement = {
      <.div(
        <.b(
        ),
        TagMod.when(page.refCount > 1 && page.networks.size > 1) {
          <.i(
            if (nlsNL) {
              s"${page.refCount} $factTypePlural in ${page.networks.size} netwerken"
            } else {
              s"${page.refCount} $factTypePlural in ${page.networks.size} networks"
            }
          )
        },
        TagMod.when(page.refCount > 0) {
          <.i(
            UiFactDescription(page.fact)
          )
        },
        if (page.networks.isEmpty) {
          noFacts()
        } else {
          UiItems(
            page.networks.map { networkFacts =>
              <.div(
                context.gotoNetworkDetails(networkFacts.networkId, networkFacts.networkName),
                TagMod.when(page.refCount > 0) {
                  <.div(
                    TagMod.when(networkFacts.factRefs.size == 1) {
                      UiThin(s"1 $factType:")
                    },
                    TagMod.when(networkFacts.factRefs.size > 1) {
                      UiThin(s"${networkFacts.factRefs.size} $factTypePlural:")
                    },
                    UiCommaList(
                      networkFacts.factRefs.map { ref =>
                        page.fact match {
                          case Fact.NodeMemberMissing => context.gotoNode(ref.id, ref.name)
                          case Fact.IntegrityCheckFailed => context.gotoNode(ref.id, ref.name)
                          case Fact.NetworkExtraMemberNode => UiOsmLink.osmNode(ref.id)
                          case Fact.NetworkExtraMemberWay =>  UiOsmLink.osmWay(ref.id)
                          case Fact.NetworkExtraMemberRelation =>  UiOsmLink.osmRelation(ref.id)
                          case _ => context.gotoRoute(ref.id, ref.name)
                        }
                      }
                    )
                  )
                }
              )
            }
          )
        }
      )
    }

    private def noFacts(): VdomElement = {
      val subset = Subset.of(page.subsetInfo.country, page.subsetInfo.networkType).get
      if (nlsNL) {
        <.span(
          s"""Er zijn geen $factTypePlural met feit "${page.fact.nlName}" in """,
          context.gotoSubsetNetworks(subset)
        )
      } else {
        <.span(
          s"""There are no $factTypePlural with fact "${page.fact.name}" in """,
          context.gotoSubsetNetworks(subset)
        )
      }
    }
  }

}
