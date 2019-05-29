// TODO migrate to Angular
package kpn.client.components.changeset

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.api.ApiClient
import kpn.client.common.ChangeSetsPageArgs
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.home.Loader
import kpn.shared.ApiResponse
import kpn.shared.changes.ChangeSetPage

object UiChangeSetsPage {

  private case class State(pageState: PageState[ChangeSetPage] = PageState())

  private case class Props(args: ChangeSetsPageArgs) {
    def changeSetId: Long = args.currentPage.changeSetId
  }

  private val component = ScalaComponent.builder[Props]("change-sets")
    .initialState(State())
    .render { scope =>
      if (scope.state.pageState.ui.status == PageStatus.Loading || scope.state.pageState.ui.status == PageStatus.LoadStarting) {
        <.div(
          <.h2("ChangeSet " + scope.props.changeSetId),
          <.div("Loading")
        )
      }
      else {
        if (scope.state.pageState.response.isEmpty) {
          <.div(
            <.h2("ChangeSet " + scope.props.changeSetId),
            <.div("Not found")
          )
        }
        else {
          <.div(
            <.h2("ChangeSet " + scope.props.changeSetId)
          )
        }
      }
    }
    .componentDidMount { scope =>
      Callback {

        scope.modState(s => s.copy(pageState = s.pageState.loadStarting())).runNow()

        def updatePageStatus(status: PageStatus.Value): Unit = {
          scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
        }

        def updateResult(response: ApiResponse[ChangeSetPage]): Unit = {
          scope.modState(s => s.copy(pageState = s.pageState.withResponse(response))).runNow()
        }

        new Loader[ApiResponse[ChangeSetPage]].load(
          ApiClient.changeSet(scope.props.changeSetId, 0 /* TODO CHANGE this was a temporary thing */),
          PageStatus.LoadStarting,
          updatePageStatus,
          updateResult
        )
      }
    }
    .build

  def apply(args: ChangeSetsPageArgs): VdomElement = component(Props(args))

}
