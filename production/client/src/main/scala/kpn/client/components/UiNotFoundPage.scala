// TODO migrate to Angular
package kpn.client.components

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nlsNL
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.PageProps
import kpn.client.components.common.PageState
import kpn.client.components.common.UiPage
import kpn.client.components.common.UiPageContents
import kpn.client.components.menu.UiAnalysisMenu
import kpn.client.components.menu.UiSidebarFooter

object UiNotFoundPage {

  private case class Props(context: Context, path: String)

  private case class State(pageState: PageState[Unit] = PageState.ready)

  private class Backend(scope: BackendScope[Props, State]) extends AbstractBackend[Unit] {

    protected def pageState: PageState[Unit] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[Unit]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    def render(props: Props, state: State): VdomElement = {
      implicit val context: Context = props.context
      new Renderer(pagePropsWithContext(props.context), state, props.path).render()
    }
  }

  private val component = ScalaComponent.builder[Props]("not-found-page")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount { scope =>
      Callback {
        scope.backend.installResizeListener()
      }
    }
    .componentWillUnmount { scope =>
      scope.backend.removeResizeListener()
    }
    .build

  def apply(context: Context, path: String): VdomElement = component(Props(context, path))

  private class Renderer(pageProps: PageProps, state: State, path: String)(implicit context: Context) {

    def render(): VdomElement = {
      UiPage(
        pageProps,
        Seq(
          UiAnalysisMenu(pageProps),
          UiSidebarFooter(pageProps)
        ),
        content()
      )
    }

    private def content(): VdomElement = {
      if (nlsNL) {
        nl()
      }
      else {
        en()
      }
    }

    private def nl(): VdomElement = {
      <.div(
        <.h1("Niet gevonden"),
        UiPageContents(
          <.p(
            "Helaas, we konden de gevraagde pagina ",
            <.i("\"" + path + "\""),
            " niet vinden."
          ),
          "Ga naar de ",
          context.gotoHome(),
          " pagina."
        )
      )
    }

    private def en(): VdomElement = {
      <.div(
        <.h1("Not found"),
        UiPageContents(
          <.p(
            "Sorry, we could not find page ",
            <.i("\"" + path + "\""),
            " on this web-site."
          ),
          "Go to the ",
          context.gotoHome(),
          " page."
        )
      )
    }
  }

}
