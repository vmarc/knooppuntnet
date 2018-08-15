package kpn.client.common.map

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^

import scalacss.ScalaCssReact._

object LayerSwitcher {

  private case class State(open: Boolean = false)

  private case class Props(layers: Seq[ol.layer.Base])

  private class Backend(scope: BackendScope[Props, State]) {

    private val open = scope.modState(_.copy(open = true))

    private val close = scope.modState(_.copy(open = false))

    def render(props: Props, state: State): VdomElement = {
      if (state.open) {
        <.div(
          UiMap.Styles.switcherPanel,
          ^.onMouseLeave --> close,
          props.layers.toTagMod { layer =>
            LayerCheckbox(layer)
          }
        )
      }
      else {
        <.div(
          ^.onMouseEnter --> open,
          ^.onMouseLeave --> close,
          <.img(
            ^.src := "/assets/images/layers.png"
          )
        )
      }
    }
  }

  private val component = ScalaComponent.builder[Props]("layer")
    .initialState(State())
    .renderBackend[Backend]
    .build

  def apply(layers: Seq[ol.layer.Base]): VdomElement = component(Props(layers))

}
