package kpn.client.components.common

import kpn.client.components.common.CssSettings.default._

object GlobalStyles extends StyleSheet.Inline {

  import dsl._

  val note: StyleA = style(
    fontStyle.italic,
    fontSize(13.px)
  )

  val rightAlign: StyleA = style(
    textAlign.right
  )

  val red: StyleA = style(
    color.red
  )

  val gray: StyleA = style(
    color.gray
  )

}
