// TODO migrate to Angular
package kpn.client.common.map

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.PageWidth

object UiEmbeddedMap {

  private case class Props(map: MapDefinition)

  private val component = ScalaComponent.builder[Props]("embedded-map")
    .render_P { props =>

      val styles = if (PageWidth.isSmall) {
        UiMap.Styles.small
      }
      else if (PageWidth.isMedium || PageWidth.isLarge) {
        UiMap.Styles.medium
      }
      else {
        UiMap.Styles.veryLarge
      }

      UiMap(UiMap.Styles.map + styles, props.map)
    }
    .build

  def apply(map: MapDefinition): VdomElement = component(Props(map))
}
