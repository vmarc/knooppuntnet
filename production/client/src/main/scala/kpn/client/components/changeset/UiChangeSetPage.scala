// TODO migrate to Angular
package kpn.client.components.changeset

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.api.ApiClient
import kpn.client.common.ChangeSetPageArgs
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.Nls.nlsNL
import kpn.client.common.User
import kpn.client.components.changeset.diff.UiNetworkUpdate
import kpn.client.components.changeset.diff.UiNodeDiffs
import kpn.client.components.changeset.diff.UiRouteDiffs
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.PageProps
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.common.UiAppBar
import kpn.client.components.common.UiHappy
import kpn.client.components.common.UiImage
import kpn.client.components.common.UiInvestigate
import kpn.client.components.common.UiLevel1
import kpn.client.components.common.UiNetworkTypeIcon
import kpn.client.components.common.UiPage
import kpn.client.components.common.UiPageContent
import kpn.client.components.common.UiPageContents
import kpn.client.components.home.Loader
import kpn.client.components.menu.UiAnalysisMenu
import kpn.client.components.menu.UiSidebarFooter
import kpn.shared.ApiResponse
import kpn.shared.ChangeSetSubsetElementRefs
import kpn.shared.ReplicationId
import kpn.shared.changes.ChangeSetPage
import org.scalajs.dom

object UiChangeSetPage {

  private case class State(pageState: PageState[ChangeSetPage] = PageState())

  private case class Props(args: ChangeSetPageArgs)

  private class Backend(scope: BackendScope[Props, State]) extends AbstractBackend[ChangeSetPage] {

    protected def pageState: PageState[ChangeSetPage] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[ChangeSetPage]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    def render(props: Props, state: State): VdomElement = {
      new Renderer(pagePropsWithContext(props.args.context), scope.props.runNow().args, state).render()
    }
  }

  private val component = ScalaComponent.builder[Props]("change-set")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount { scope =>
      Callback {

        User.get match {

          case None =>

            scope.modState(s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(status = PageStatus.NotAuthorized, isSideBarOpen = false), response = None))).runNow()

          case Some(user) =>

            scope.backend.installResizeListener()

            scope.modState(s => s.copy(pageState = s.pageState.loadStarting())).runNow()

            def updatePageStatus(status: PageStatus.Value): Unit = {
              scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
            }

            def updateResult(response: ApiResponse[ChangeSetPage]): Unit = {
              scope.modState(s => s.copy(pageState = s.pageState.withResponse(response))).runNow()
            }

            new Loader[ApiResponse[ChangeSetPage]].load(
              ApiClient.changeSet(scope.props.args.changeSetId, scope.props.args.replicationNumber),
              PageStatus.LoadStarting,
              updatePageStatus,
              updateResult
            )
        }
      }
    }
    .componentDidUpdate { scope =>
      CallbackTo {
        dom.window.setTimeout(() => {
          val element = dom.document.getElementById(scope.prevProps.args.networkId.toString)
          if (element != null) {
            element.scrollIntoView()
            dom.window.scrollBy(0, -(UiAppBar.appBarHeight + 10))
          }
        }, 40)
      }
    }
    .componentWillUnmount { scope =>
      scope.backend.removeResizeListener()
    }
    .build

  def apply(args: ChangeSetPageArgs): VdomElement = component(Props(args))

  private class Renderer(pageProps: PageProps, args: ChangeSetPageArgs, state: State) {

    private implicit val context: Context = args.context

    def render(): VdomElement = {

      val sideBar = Seq(
        UiAnalysisMenu(pageProps),
        UiSidebarFooter(pageProps)
      )

      val content = UiPageContent(
        title,
        state.pageState.ui.status,
        CallbackTo {
          contents()
        }
      )

      UiPage(
        pageProps,
        sideBar,
        content
      )
    }

    private def title = nls("ChangeSet", "Wijzigingenset") + "  " + args.changeSetId + " " + ReplicationId(args.replicationNumber).name

    private def contents(): TagMod = {
      state.pageState.response.whenDefined { response =>
        response.result.whenDefined { changeSetPage =>
          <.div(
            <.h1(title),
            //changeSetPopups(),
            UiPageContents(
              UiChangeSetHeader(args, changeSetPage),
              networkDiffDetails(changeSetPage),
              orphanNodeChanges(changeSetPage),
              orphanRouteChanges(changeSetPage)
            )
          )
        }
      }
    }

    private def changeSetPopups(): VdomElement = {

      <.div(
        ^.cls := "kpn review-pending ui wide popup",
        <.div(^.cls := "header", "Status: New"),
        <.div(
          ^.cls := "popup-logo",
          UiImage("review-in-progress-64.png")
        ),
        if (nlsNL) {
          <.div(
            "TODO"
          )
        } else {
          <.div(
            "The changeset has not been reviewed by anybody yet. If the changes in the changeset do not have " +
              "any impact on the analysis results (indicated by the ",
            UiHappy(),
            " and ",
            UiInvestigate(),
            " icons), then the changeset can be left in this status. No review is needed."
          )
        },
        <.div(
          ^.cls := "kpn review-in-progress ui wide popup",
          <.div(^.cls := "header", "Review in progress"),
          <.div(
            ^.cls := "popup-logo",
            UiImage("review-in-progress-64.png")
          ),
          if (nlsNL) {
            <.div(
              "TODO"
            )
          } else {
            <.div(
              "An initial review of the changeset is done, but further actions are being undertaken to fix any problems " +
                "that have been left behind. Set this status to indicate to other users that you are fixing these problems, " +
                "and that further review from another user is not necessary. The idea is that after the problems have been " +
                """fixed, that the status is changed to "Reviewed"."""
            )
          }
        ),
        <.div(
          ^.cls := "kpn reviewed ui wide popup",
          <.div(
            ^.cls := "header",
            "Reviewed"
          ),
          <.div(
            ^.cls := "popup-logo",
            UiImage("reviewed-64.png")
          ),
          if (nlsNL) {
            <.div(
              "TODO"
            )
          } else {
            <.div(
              "The changeset has been reviewed and issues (if any) have been resolved. There is no need for another user " +
                "to still review this changeset."
            )
          }
        )
      )
    }

    private def networkDiffDetails(page: ChangeSetPage): TagMod = {
      page.networkChanges.toTagMod { networkChange =>
        UiNetworkUpdate(
          page.summary,
          networkChange,
          page.routeChanges,
          page.nodeChanges,
          page.knownElements
        )
      }
    }

    private def orphanNodeChanges(page: ChangeSetPage): TagMod = {
      page.summary.orphanNodeChanges.toTagMod { changeSetSubsetElementRefs =>
        UiLevel1(
          orphansHeader(nls("Orphan nodes", "Knooppunt wezen"), changeSetSubsetElementRefs),
          UiNodeDiffs(
            page.summary.key.changeSetId,
            changeSetSubsetElementRefs.subset.name,
            changeSetSubsetElementRefs.elementRefs.toRefDiffs,
            page.nodeChanges,
            page.knownElements
          )
        )
      }
    }

    private def orphanRouteChanges(page: ChangeSetPage): TagMod = {

      page.summary.orphanRouteChanges.toTagMod { changeSetSubsetElementRefs =>
        UiLevel1(
          orphansHeader(nls("Orphan routes", "Route wezen"), changeSetSubsetElementRefs),
          UiRouteDiffs(
            page.summary.key.changeSetId,
            changeSetSubsetElementRefs.subset.name,
            changeSetSubsetElementRefs.elementRefs.toRefDiffs,
            page.routeChanges,
            page.knownElements
          )
        )
      }
    }

    private def orphansHeader(title: String, changeSetSubsetElementRefs: ChangeSetSubsetElementRefs): TagMod = {
      <.div(
        UiNetworkTypeIcon(changeSetSubsetElementRefs.subset.networkType),
        " ",
        changeSetSubsetElementRefs.subset.country.domain.toUpperCase(),
        " ",
        title
      )
    }
  }

}
