package kpn.client.components.menu

import chandu0101.scalajs.react.components.materialui.MuiMenuItem
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomNode
import kpn.client.RouteConfiguration.Goto
import kpn.client.common.Context

import scala.scalajs.js

object UiMenuItem {

  def apply(title: String, selected: Boolean, count: Option[Int], goto: Goto)(implicit context: Context): VdomElement = {

    val countString: VdomNode = count.fold("")(_.toString)

    val titleNode: VdomNode = title

    MuiMenuItem[String](
      key = title,
      style = if (selected) js.Dynamic.literal(backgroundColor = "#f0f0f0") else js.undefined,
      desktop = true,
      primaryText = titleNode,
      onTouchTap = context.tempGetRouter.setEH(goto),
      secondaryText = countString
    )()
  }
}
