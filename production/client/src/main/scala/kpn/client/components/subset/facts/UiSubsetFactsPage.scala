package kpn.client.components.subset.facts

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.api.ApiClient
import kpn.client.common.Nls.nls
import kpn.client.common.SubsetPageArgs
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.common.UiFactLevel
import kpn.client.components.common.UiItems
import kpn.client.components.common.UiPageContents
import kpn.client.components.common.UiSituationOn
import kpn.client.components.facts.UiFactDescription
import kpn.client.components.home.Loader
import kpn.client.components.subset.SubsetInfoCache
import kpn.client.components.subset.SubsetPageProps
import kpn.client.components.subset.SubsetPageRenderer
import kpn.client.components.subset.UiSubsetMenu
import kpn.shared.ApiResponse
import kpn.shared.Fact
import kpn.shared.FactCount
import kpn.shared.TimeInfo
import kpn.shared.subset.SubsetFactsPage

object UiSubsetFactsPage {

  private case class State(pageState: PageState[SubsetFactsPage] = PageState())

  private class Backend(scope: BackendScope[SubsetPageArgs, State]) extends AbstractBackend[SubsetFactsPage] {

    protected def pageState: PageState[SubsetFactsPage] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[SubsetFactsPage]): Unit = {
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

      scope.modState(s => s.copy(pageState = s.pageState.loadStarting())).runNow()

      def updatePageStatus(status: PageStatus.Value): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
      }

      def updateResult(response: ApiResponse[SubsetFactsPage]): Unit = {
        response.result.foreach { page =>
          SubsetInfoCache.put(props.subset, page.subsetInfo)
        }
        scope.modState(s => s.copy(pageState = s.pageState.withResponse(response))).runNow()
      }

      new Loader[ApiResponse[SubsetFactsPage]].load(
        ApiClient.subsetFacts(props.subset),
        PageStatus.LoadStarting,
        updatePageStatus,
        updateResult
      )
    }
  }

  private val component = ScalaComponent.builder[SubsetPageArgs]("subset-facts")
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

    protected def contents(): VdomElement = {
      <.div(
        <.h1(title),
        <.h2(nls("Facts", "Feiten")),
        UiPageContents(
          UiSituationOn(state.pageState.situationOn),
          state.pageState.response.whenDefined { response =>
            response.result.whenDefined { page =>
              if (page.factCounts.isEmpty) {
                <.i("No facts")
              }
              else {
                facts(page)
              }
            }
          }
        )
      )
    }

    private def facts(page: SubsetFactsPage): VdomElement = {
      UiItems(
        page.factCounts.map { case FactCount(fact, count) =>
          val title = nls(fact.name, fact.nlName)
          val link = fact match {
            case Fact.OrphanNode => context.gotoSubsetOrphanNodes(props.args.subset, title)
            case Fact.OrphanRoute => context.gotoSubsetOrphanRoutes(props.args.subset, title)
            case _ => context.gotoSubsetFactDetails(props.args.subset, fact, title)
          }

          <.div(
            link,
            s" ($count) ",
            UiFactLevel(fact.level),
            UiFactDescription(fact)
          )
        }
      )
    }
  }

}
