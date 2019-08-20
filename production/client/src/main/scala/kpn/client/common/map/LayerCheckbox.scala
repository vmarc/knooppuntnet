// Migrated to Angular: layer-switcher.component.ts
package kpn.client.common.map

import chandu0101.scalajs.react.components.Implicits._
import chandu0101.scalajs.react.components.materialui.MuiCheckbox
import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.CallbackTo
import japgolly.scalajs.react.ReactMouseEvent
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<

import scala.scalajs.js

object LayerCheckbox {

  private case class Props(layer: ol.layer.Base)

  private case class State(selected: Boolean = true)

  private class Backend(scope: BackendScope[Props, State]) {

    private val onCheckLayer = (event: ReactMouseEvent, checked: Boolean) => {
      CallbackTo {
        scope.props.runNow().layer.setVisible(checked)
        scope.setState(State(checked)).runNow()
      }
    }

    def render(props: Props, state: State): VdomElement = {

      val layerName = props.layer.get("name").toString

      <.div(
        MuiCheckbox(
          key = layerName,
          checked = props.layer.getVisible(),
          iconStyle = js.Dynamic.literal(
            fill = "#000"
          ),
          label = <.span(layerName),
          onCheck = onCheckLayer
        )()
      )
    }
  }

  private val component = ScalaComponent.builder[Props]("layer-checkbox")
    .initialState(State())
    .renderBackend[Backend]
    .build

  def apply(layer: ol.layer.Base): VdomElement = {
    component(Props(layer))
  }
}
