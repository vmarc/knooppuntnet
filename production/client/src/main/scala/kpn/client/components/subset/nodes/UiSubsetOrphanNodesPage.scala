// Migrated to Angular: _subset-orphan-nodes-page.component.ts
package kpn.client.components.subset.nodes

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.api.ApiClient
import kpn.client.common.Nls.nls
import kpn.client.common.SubsetPageArgs
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.common.UiHappy
import kpn.client.components.common.UiItems
import kpn.client.components.common.UiPageContents
import kpn.client.components.common.UiSituationOn
import kpn.client.components.filter.UiFilter
import kpn.client.components.home.Loader
import kpn.client.components.subset.SubsetInfoCache
import kpn.client.components.subset.SubsetPageProps
import kpn.client.components.subset.SubsetPageRenderer
import kpn.client.components.subset.UiSubsetMenu
import kpn.shared.ApiResponse
import kpn.shared.NodeInfo
import kpn.shared.TimeInfo
import kpn.shared.subset.SubsetOrphanNodesPage

object UiSubsetOrphanNodesPage {

  private case class State(
    pageState: PageState[SubsetOrphanNodesPage] = PageState(),
    filterCriteria: SubsetOrphanNodeFilterCriteria = SubsetOrphanNodeFilterCriteria()
  )

  private class Backend(scope: BackendScope[SubsetPageArgs, State]) extends AbstractBackend[SubsetOrphanNodesPage] {

    protected def pageState: PageState[SubsetOrphanNodesPage] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[SubsetOrphanNodesPage]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    private def updateCriteria(criteria: SubsetOrphanNodeFilterCriteria): Unit = {
      scope.modState(s => s.copy(filterCriteria = criteria)).runNow()
    }

    def render(args: SubsetPageArgs, state: State): VdomElement = {

      val pageProps = pagePropsWithContext(args.context, hasFilter = true)

      val props = SubsetPageProps(
        state.pageState.response.flatMap(_.result.map(_.timeInfo)).getOrElse(TimeInfo()),
        args,
        pageProps
      )

      new Renderer(props, state, updateCriteria).render()
    }

    def retrieve(props: SubsetPageArgs): Unit = {

      scope.modState(s => s.copy(pageState = s.pageState.loadStarting())).runNow()

      def updatePageStatus(status: PageStatus.Value): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
      }

      def updateResult(response: ApiResponse[SubsetOrphanNodesPage]): Unit = {
        response.result.foreach { page =>
          SubsetInfoCache.put(props.subset, page.subsetInfo)
        }
        scope.modState(s => s.copy(pageState = s.pageState.withResponse(response))).runNow()
      }

      new Loader[ApiResponse[SubsetOrphanNodesPage]].load(
        ApiClient.subsetOrphanNodes(props.subset),
        PageStatus.LoadStarting,
        updatePageStatus,
        updateResult
      )
    }
  }

  private val component = ScalaComponent.builder[SubsetPageArgs]("subset-orphan-nodes-page")
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

  private class Renderer(props: SubsetPageProps, state: State, updateCriteria: (SubsetOrphanNodeFilterCriteria) => Unit)
    extends SubsetPageRenderer(UiSubsetMenu.targetOrphanNodes, state.pageState.ui.status)(props) {

    private val nodes = state.pageState.response.toSeq.flatMap(_.result.map(_.rows)).flatten

    private val filterX = new SubsetOrphanNodeFilter(props.timeInfo, state.filterCriteria, updateCriteria)
    private val filteredNodes = filterX.filter(nodes)
    private val filterOptions = filterX.filterOptions(nodes)

    protected def contents(): VdomElement = {
      <.div(
        <.h1(title),
        <.h2(nls("Orphan nodes", "Knooppuntwezen")),
        UiPageContents(
          UiSituationOn(state.pageState.situationOn),
          nodes(filteredNodes)
        )
      )
    }

    private def nodes(nodes: Seq[NodeInfo]): VdomElement = {
      if (nodes.isEmpty) {
        <.div(
          <.br(),
          UiHappy(),
          " ",
          nls("No orphan nodes", "Geen knooppuntwezen")
        )
      }
      else {
        UiItems(
          nodes.map { node =>
            UiSubsetOrphanNodeRow(props.args.subset.networkType, node)
          }
        )
      }
    }

    override protected def filter(): VdomElement = {
      state.pageState.response match {
        case None => <.span()
        case Some(page) =>
          new UiFilter.FilterRenderer(filterOptions).render()
      }
    }
  }

}
