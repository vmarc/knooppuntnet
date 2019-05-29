// TODO migrate to Angular
package kpn.client.components.network.changes

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.PageTitleBuilder
import kpn.client.api.ApiClient
import kpn.client.common.BrowserLocalStorage
import kpn.client.common.Context
import kpn.client.common.NetworkPageArgs
import kpn.client.common.Nls.nls
import kpn.client.common.User
import kpn.client.components.changes.UiImpactToggle
import kpn.client.components.changes.filter.UiChangeFilter
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.common.PageWidth
import kpn.client.components.common.UiHappy
import kpn.client.components.common.UiInvestigate
import kpn.client.components.common.UiItems
import kpn.client.components.common.UiLine
import kpn.client.components.common.UiPageContents
import kpn.client.components.common.UiPager
import kpn.client.components.common.UiSituationOn
import kpn.client.components.common.UiThick
import kpn.client.components.common.UiThin
import kpn.client.components.home.Loader
import kpn.client.components.network.NetworkNameCache
import kpn.client.components.network.NetworkPageRenderer
import kpn.client.components.network.NetworkSummaryCache
import kpn.client.components.network.UiNetworkMenu
import kpn.client.components.shared.UiChangeSet
import kpn.shared.ApiResponse
import kpn.shared.changes.details.NetworkChangeInfo
import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.network.NetworkChangesPage
import kpn.shared.network.NetworkSummary
import scalacss.ScalaCssReact._

object UiNetworkChangesPage {

  private case class State(
    pageState: PageState[NetworkChangesPage] = PageState(),
    parameters: Option[ChangesParameters] = None
  )

  private class Backend(scope: BackendScope[NetworkPageArgs, State]) extends AbstractBackend[NetworkChangesPage] {

    private implicit val context: Context = scope.props.runNow().context

    protected def pageState: PageState[NetworkChangesPage] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[NetworkChangesPage]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    def currentPage: Option[NetworkChangesPage] = {
      scope.state.runNow().pageState.response.flatMap(_.result)
    }

    private def update(parameters: ChangesParameters): Unit = {


      User.get match {
        case None =>

          scope.modState(
            s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(status = PageStatus.NotAuthorized, isSideBarOpen = false), response = None))).runNow()

        case Some(user) =>

          scope.modState(s => s.copy(parameters = Some(parameters), pageState = s.pageState.withStatus(PageStatus.UpdateStarting))).runNow()

          def updatePageStatus(status: PageStatus.Value): Unit = {
            scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
          }

          def updateResult(response: ApiResponse[NetworkChangesPage]): Unit = {
            scope.modState(s => s.copy(pageState = s.pageState.withResponse(response))).runNow()
          }

          new Loader[ApiResponse[NetworkChangesPage]].load(
            ApiClient.networkChanges(parameters),
            PageStatus.UpdateStarting,
            updatePageStatus,
            updateResult
          )
      }
    }

    private def currentParameters: Option[ChangesParameters] = scope.state.runNow().parameters

    def filterChanged(year: Option[String], month: Option[String], day: Option[String], impact: Boolean): Unit = {
      BrowserLocalStorage.impact = impact
      currentParameters.foreach(p => update(p.copy(year = year, month = month, day = day, impact = impact, pageIndex = 0)))
    }

    def itemsPerPageChanged(itemsPerPage: Int): Unit = {
      BrowserLocalStorage.itemsPerPage = itemsPerPage
      currentParameters.foreach(p => update(p.copy(itemsPerPage = itemsPerPage, pageIndex = 0)))
    }

    def pageIndexChanged(pageIndex: Int): Unit = {
      currentParameters.foreach(p => update(p.copy(pageIndex = pageIndex)))
    }

    private val onToggleImpact = (event: ReactMouseEvent, newImpact: Boolean) => {
      CallbackTo {
        BrowserLocalStorage.impact = newImpact
        currentParameters.foreach(p => update(p.copy(impact = newImpact)))
      }
    }

    def filter()(implicit context: Context): VdomElement = {
      currentPage match {
        case Some(page) =>
          currentParameters match {
            case Some(parameters) =>
              UiChangeFilter(
                page.filter,
                filterChanged,
                parameters.itemsPerPage,
                itemsPerPageChanged
              )
            case None => <.div()
          }
        case None => <.div()
      }
    }


    def render(args: NetworkPageArgs, state: State): VdomElement = {

      val pageProps = pagePropsWithContext(args.context, hasFilter = true)

      val networkSummary = state.pageState.response match {
        case None => NetworkSummaryCache.get(args.networkId)
        case Some(apiResponse) =>
          apiResponse.result.map { page =>
            val summary = page.network.detail match {
              case None =>
                NetworkSummary(
                  page.network.attributes.networkType,
                  page.network.attributes.name,
                  page.network.factCount,
                  0,
                  0
                )
              case Some(detail) =>
                NetworkSummary(
                  page.network.attributes.networkType,
                  page.network.attributes.name,
                  page.network.factCount,
                  detail.nodes.size,
                  detail.routes.size
                )
            }
            NetworkSummaryCache.put(args.networkId, summary)
            NetworkNameCache.put(args.networkId, summary.name)
            PageTitleBuilder.setNetworkPageTitle(nls("Changes", "Wijzigingen"), summary.name)
            summary
          }
      }

      val props = NetworkChangesProps(
        args,
        pageProps,
        NetworkNameCache.get(args.networkId),
        networkSummary
      )

      new Renderer(props, state, filter(), pageIndexChanged, onToggleImpact).render()
    }

    def retrieve(props: NetworkPageArgs): Unit = {

      User.get match {
        case None =>
          scope.modState(
            s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(status = PageStatus.NotAuthorized, isSideBarOpen = false), response = None))).runNow()

        case Some(user) =>
          val parameters = ChangesParameters(
            networkId = Some(props.networkId),
            itemsPerPage = BrowserLocalStorage.itemsPerPage,
            impact = BrowserLocalStorage.impact
          )

          scope.modState(s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(status = PageStatus.LoadStarting, isSideBarOpen = false), response = None),
            parameters = Some(parameters))).runNow()

          def updatePageStatus(status: PageStatus.Value): Unit = {
            scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
          }

          def updateResult(response: ApiResponse[NetworkChangesPage]): Unit = {
            scope.modState(s => s.copy(pageState = s.pageState.withResponse(response))).runNow()
          }

          new Loader[ApiResponse[NetworkChangesPage]].load(
            ApiClient.networkChanges(parameters),
            PageStatus.LoadStarting,
            updatePageStatus,
            updateResult
          )
      }
    }
  }

  private val component = ScalaComponent.builder[NetworkPageArgs]("network-changes")
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

  private class Renderer(
    props: NetworkChangesProps,
    state: State,
    changesFilter: VdomElement,
    pageIndexChanged: (Int) => Unit,
    onToggleImpact: (ReactMouseEvent, Boolean) => Callback
  ) extends NetworkPageRenderer(UiNetworkMenu.changes, state.pageState.ui.status, props.networkName, props.networkSummary)(props) {

    override protected def filter(): VdomElement = {
      changesFilter
    }

    private def currentParameters = state.parameters

    protected def contents(): VdomElement = {
      if (state.pageState.response.get.result.get.changes.isEmpty) {
        <.div("no changes")
      }
      else {
        val from = state.parameters match {
          case Some(parameters) => parameters.itemsPerPage * parameters.pageIndex
          case None => 0
        }

        <.div(
          <.h1(state.pageState.response.get.result.get.network.attributes.name),
          <.h2(nls("Changes", "Wijzigingen")),
          UiPageContents(
            impact(),
            UiSituationOn(state.pageState.situationOn),
            <.br(),
            fromTo(),
            UiPager(
              state.parameters.get.itemsPerPage,
              state.pageState.response.get.result.get.totalCount,
              state.parameters.get.pageIndex,
              pageIndexChanged
            ),
            UiItems(
              state.pageState.response.get.result.get.changes.map(summary),
              from
            ),
            UiPager(
              state.parameters.get.itemsPerPage,
              state.pageState.response.get.result.get.totalCount,
              state.parameters.get.pageIndex,
              pageIndexChanged
            )
          )
        )
      }
    }

    private def impact(): VdomElement = {
      UiImpactToggle(currentParameters.get.impact, onToggleImpact)
    }

    private def fromTo(): VdomElement = {
      val totalItemCount = state.pageState.response.get.result.get.totalCount
      val from = state.parameters.get.itemsPerPage * state.parameters.get.pageIndex + 1
      val to = totalItemCount.min(state.parameters.get.itemsPerPage * (state.parameters.get.pageIndex + 1))
      if (totalItemCount < 2) {
        <.div()
      }
      else {
        <.div(
          ^.cls := "ui segment",
          <.div(
            <.i(
              nls("changes", "wijzigingen"),
              s" $from ",
              nls("to", "tot"),
              s"  $to ",
              nls("of", "van"),
              s" $totalItemCount"
            )
          )
        )
      }
    }


    private def summary(change: NetworkChangeInfo): VdomElement = {

      val key = change.key

      <.div(
        TagMod.when(PageWidth.isSmall) {
          <.div(
            UiLine(
              UiThick(context.gotoChangeSet(key.changeSetId, key.replicationNumber)),
              TagMod.when(change.happy)(UiHappy()),
              TagMod.when(change.investigate)(UiInvestigate())
            ),
            <.div(
              UiThin(key.timestamp.yyyymmddhhmm)
            )
          )
        },
        TagMod.when(!PageWidth.isSmall) {
          UiLine(
            UiThick(context.gotoChangeSet(key.changeSetId, key.replicationNumber)),
            UiThin(key.timestamp.yyyymmddhhmm),
            TagMod.when(change.happy)(UiHappy()),
            TagMod.when(change.investigate)(UiInvestigate())
          )
        },
        TagMod.when(change.comment.isDefined) {
          <.div(
            UiChangeSet.Styles.comment,
            change.comment.get
          )
        },
        UiNetworkChange(change)
      )
    }
  }

}
