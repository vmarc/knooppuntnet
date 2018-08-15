package kpn.client.components.subset.networks

import chandu0101.scalajs.react.components.Implicits._
import chandu0101.scalajs.react.components.materialui.MuiDialog
import chandu0101.scalajs.react.components.materialui.MuiFlatButton
import chandu0101.scalajs.react.components.materialui.TouchTapEvent
import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.map.UiBigMap
import kpn.shared.common.Ref
import kpn.shared.subset.SubsetNetworksPage

object UiSubsetNetworksMap {

  private case class State(network: Option[Ref] = None)

  private case class Props(context: Context, page: SubsetNetworksPage)

  private class Backend(scope: BackendScope[Props, State]) {

    private val closeDescription1 = (event: TouchTapEvent) => {
      event.preventDefault()
      scope.setState(State(None))
    }
    private val closeDescription2 = (open: Boolean) => scope.setState(State(None))

    def render(props: Props, state: State): VdomElement = {
      implicit val context: Context = props.context

      <.div(
        UiBigMap(new SubsetNetworksMap(props.page.networks)),
        MuiDialog(
          actions = MuiFlatButton(key = "1", label = nls("Close", "Sluiten"), secondary = true, onTouchTap = closeDescription1)(),
          open = state.network.isDefined,
          onRequestClose = closeDescription2
        )(
          <.div(
            TagMod.when(state.network.isDefined) {
              <.div(
                state.network.get.name
              )
            },
            TagMod.when(state.network.isDefined) {
              <.div(
                context.gotoNetworkDetails(state.network.get.id, nls("Show network details", "Toon netwerk details"))
              )
            }
          )
        )
      )
    }
  }

  private val component = ScalaComponent.builder[Props]("subset-map")
    .initialState(State())
    .renderBackend[Backend]
    .build

  def apply(page: SubsetNetworksPage)(implicit context: Context): VdomElement = component(Props(context, page))
}
