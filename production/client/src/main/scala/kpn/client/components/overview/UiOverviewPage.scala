// TODO migrate to Angular
package kpn.client.components.overview

import chandu0101.scalajs.react.components.materialui.MuiDivider
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.api.ApiClient
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.OverviewPageArgs
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.PageState
import kpn.client.components.common.PageStatus
import kpn.client.components.common.PageWidth
import kpn.client.components.common.UiPage
import kpn.client.components.common.UiPageContent
import kpn.client.components.common.UiPageContents
import kpn.client.components.common.UiSituationOn
import kpn.client.components.home.Loader
import kpn.client.components.menu.UiAnalysisMenu
import kpn.client.components.menu.UiSidebarFooter
import kpn.shared.ApiResponse
import kpn.shared.statistics.Statistics
import scalacss.defaults.Exports.StyleSheet

object UiOverviewPage {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val items: StyleA = style(
      marginTop(20.px),
      borderTopColor.lightgray,
      borderTopStyle.solid,
      borderTopWidth(1.px),
      media.maxWidth(PageWidth.SmallMaxWidth.px)(
        marginLeft((-UiPage.smallContentsMargin).px),
        marginRight((-UiPage.smallContentsMargin).px)
      )
    )


    val item: StyleA = style(
      padding(10.px),
      borderBottomColor.lightgray,
      borderBottomStyle.solid,
      borderBottomWidth(1.px)
    )

    val title: StyleA = style(
      fontWeight.bold
    )

    val comment: StyleA = style(
      marginTop(20.px),
      marginBottom(20.px),
      marginLeft(10.px),
      marginRight(20.px),
      paddingLeft(5.px),
      borderLeftColor.lightgray,
      borderLeftStyle.solid,
      borderLeftWidth(3.px),
      unsafeChild("p")(
        &.firstChild(
          marginTop(0.px)
        ),
        &.lastChild(
          marginBottom(0.px)
        )
      )
    )

    val commentCell: StyleA = style(
      unsafeChild("p")(
        &.firstChild(
          marginTop(0.px)
        ),
        &.lastChild(
          marginBottom(0.px)
        )
      )
    )

    val valueColumn: StyleA = style(
      textAlign.right,
      verticalAlign.middle,
      width(3.5.em)
    )

    val valueCell: StyleA = style(
      textAlign.left,
      verticalAlign.middle,
      width(3.5.em)
    )
  }

  private case class Props(context: Context)

  private case class State(pageState: PageState[Statistics] = PageState())

  private class Backend(scope: BackendScope[Props, State]) extends AbstractBackend[Statistics] {

    protected def pageState: PageState[Statistics] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[Statistics]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    def render(props: Props, state: State): VdomElement = {

      implicit val context: Context = props.context
      val pageProps = pagePropsWithContext(props.context)

      val content = UiPageContent(
        nls("Overview in numbers", "Overzicht in cijfers"),
        state.pageState.ui.status,
        CallbackTo {
          <.div(
            <.h1(nls("Overview in numbers", "Overzicht in cijfers")),
            UiPageContents(
              UiSituationOn(state.pageState.situationOn),
              <.br(),
              state.pageState.response.whenDefined { response =>
                response.result.whenDefined { page =>
                  if (PageWidth.isLarge || PageWidth.isVeryLarge) {
                    UiOverviewTable(page)
                  }
                  else {
                    UiOverviewList(page)
                  }
                }
              }
            )
          )
        }
      )

      def sideBar(): Seq[VdomElement] = {
        Seq(
          UiAnalysisMenu(pageProps),
          MuiDivider()(),
          UiSidebarFooter(pageProps)
        )
      }

      UiPage(
        pageProps,
        sideBar(),
        content
      )
    }

    def retrieve(props: Props): Unit = {

      scope.modState(s => s.copy(pageState = s.pageState.loadStarting())).runNow()

      def updatePageStatus(status: PageStatus.Value): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
      }

      def updateResult(response: ApiResponse[Statistics]): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withResponse(response))).runNow()
      }

      new Loader[ApiResponse[Statistics]].load(
        ApiClient.overview(),
        PageStatus.LoadStarting,
        updatePageStatus,
        updateResult
      )
    }
  }

  private val component = ScalaComponent.builder[Props]("overview")
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

  def apply(args: OverviewPageArgs): VdomElement = component(Props(args.context))
}
