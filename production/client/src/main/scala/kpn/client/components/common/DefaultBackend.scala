// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.CallbackTo
import japgolly.scalajs.react.vdom.VdomElement
import kpn.client.common.Context
import kpn.client.components.menu.UiAnalysisMenu
import kpn.client.components.menu.UiSidebarFooter

abstract class DefaultBackend(scope: BackendScope[DefaultProps, PageState[Unit]]) extends AbstractBackend[Unit] {

  protected implicit val context: Context = scope.props.runNow().context

  protected def pageState: PageState[Unit] = scope.state.runNow()

  protected def updatePageState(pageState: PageState[Unit]): Unit = {
    scope.setState(pageState).runNow()
  }

  protected def contents(props: DefaultProps, state: PageState[Unit]): VdomElement

  protected def title: String

  def render(props: DefaultProps, state: PageState[Unit]): VdomElement = {

    implicit val context: Context = props.context

    val content = UiPageContent(
      title,
      state.ui.status,
      CallbackTo {
        contents(props, state)
      }
    )

    val pageProps = pagePropsWithContext(context)

    UiPage(
      pageProps,
      Seq(
        UiAnalysisMenu(pageProps),
        UiSidebarFooter(pageProps)
      ),
      content
    )
  }
}
