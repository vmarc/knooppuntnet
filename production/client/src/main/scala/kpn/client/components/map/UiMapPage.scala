// TODO migrate to Angular
package kpn.client.components.map

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import kpn.client.common.Context
import kpn.client.common.MapPageArgs
import kpn.client.common.map.UiFullMap
import kpn.client.common.map.vector.SelectedFeatureHolder
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.PageState
import kpn.client.components.common.UiPage

object UiMapPage {

  private case class State(pageState: PageState[Unit] = PageState())

  private case class Props(args: MapPageArgs)

  private class Backend(scope: BackendScope[Props, State]) extends AbstractBackend[Unit] {

    val selectionHolder: SelectedFeatureHolder = new SelectedFeatureHolder()

    protected def pageState: PageState[Unit] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[Unit]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    def render(props: Props, state: State): VdomElement = {

      implicit val context: Context = props.args.context

      val pageProps = pagePropsWithContext(props.args.context)

      UiPage(
        pageProps,
        Seq(
          UiMapDetail(props.args.networkType, selectionHolder)
        ),
        UiFullMap(new MainMap(props.args.networkType, selectionHolder))
      )
    }
  }

  private val component = ScalaComponent.builder[Props]("map")
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

  def apply(args: MapPageArgs): VdomElement = component(Props(args))
}
