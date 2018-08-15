package kpn.client.components.common

import chandu0101.scalajs.react.components.materialui.Mui
import chandu0101.scalajs.react.components.materialui.MuiAvatar
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import kpn.shared.FactLevel

object UiFactLevel {

  private val component = ScalaComponent.builder[FactLevel]("fact-level")
    .render_P { props =>
      MuiAvatar(
        size = 12,
        backgroundColor = props match {
          case FactLevel.ERROR => Mui.Styles.colors.red400
          case FactLevel.INFO => Mui.Styles.colors.green400
          case _ => Mui.Styles.colors.orange400
        }
      )()
    }
    .build

  def apply(level: FactLevel): VdomElement = {
    component(level)
  }
}
