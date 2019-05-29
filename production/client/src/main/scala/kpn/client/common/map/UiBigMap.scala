// TODO migrate to Angular
package kpn.client.common.map

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.PageWidth

/*
  A map that takes as much screen estate as possible. At the top of the page it
  leaves room for the appbar and a page title. On larger screens, it leaves
  room for the left menu-bar.
 */
object UiBigMap {

  private case class Props(map: MapDefinition)

  private val component = ScalaComponent.builder[Props]("big-map")
    .render_P { props =>
      val styles = if (PageWidth.isSmall) {
        UiMap.Styles.small
      }
      else {
        UiMap.Styles.medium
      }

      UiMap(UiMap.Styles.map + styles, props.map)
    }
    .build

  def apply(map: MapDefinition): VdomElement = component(Props(map))
}
