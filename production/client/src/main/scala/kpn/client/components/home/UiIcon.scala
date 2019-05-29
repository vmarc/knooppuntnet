// TODO migrate to Angular
package kpn.client.components.home

import chandu0101.scalajs.react.components.CssProperties
import chandu0101.scalajs.react.components.materialui.MuiColor
import chandu0101.scalajs.react.components.materialui.MuiSvgIcon
import chandu0101.scalajs.react.components.materialui.MuiSvgIcon.MuiSvgIcon
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement

import scala.scalajs.js.UndefOr
import scala.scalajs.js.undefined

object UiIcon {

  def apply(
    icon: MuiSvgIcon,
    color: UndefOr[MuiColor] = undefined,
    style: UndefOr[CssProperties] = undefined
  ): VdomElement = {
    new MuiSvgIcon.SvgIconApply(icon).apply(color = color, style = style)()
  }
}
