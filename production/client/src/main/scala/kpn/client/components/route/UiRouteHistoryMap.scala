// TODO migrate to Angular
package kpn.client.components.route

import kpn.client.components.common.CssSettings.default._

object UiRouteHistoryMap {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val historyMapNote: StyleA = style(
      paddingTop(5.px),
      fontStyle.italic,
      fontSize(13.px),
      backgroundColor.red
    )
  }

}
