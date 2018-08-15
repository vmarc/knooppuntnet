package kpn.client.components.login

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.Path
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.components.common.DefaultProps
import org.scalajs.dom

object UiAuthenticatePage {

  private case class State()

  private class Backend(scope: BackendScope[DefaultProps, State]) {

    def retrieve(props: DefaultProps): Unit = {
      implicit val context: Context = props.context
      import scala.concurrent.ExecutionContext.Implicits.global

      val search = dom.window.location.search

      dom.ext.Ajax.get(
        url = "/authenticated" + search,
        timeout = 25000
      ).map { r =>
        val withoutQuestionMark = search.drop(1)
        val firstParam = withoutQuestionMark.split("&")(0)
        val keyAndValue = firstParam.split("=")
        val page = keyAndValue(1).drop(1) // drop 1 is to loose the leading slash
        context.tempGetRouter.byPath.set(Path(page)).runNow()
      }
    }

    def render(props: DefaultProps, state: State): VdomElement = {
      retrieve(props)
      <.div("") // TODO LOGIN - now showing empty page - should show something (including button to navigate home?) if request to server fails
    }
  }

  private val component = ScalaComponent.builder[DefaultProps]("authenticate")
    .initialState(State())
    .renderBackend[Backend]
    .build

  def apply(context: Context): VdomElement = component(DefaultProps(context))

}
