// TODO migrate to Angular
package kpn.client.components.home

import chandu0101.scalajs.react.components.materialui.Mui.Styles.colors.grey800
import chandu0101.scalajs.react.components.materialui.Mui.SvgIcons.ActionRestore
import japgolly.scalajs.react.vdom.VdomElement

import scala.scalajs.js

object UiChangesIcon {

  private val style = js.Dynamic.literal(
    verticalAlign = "text-bottom"
  )

  def apply(): VdomElement = UiIcon(ActionRestore, grey800, style)

}
