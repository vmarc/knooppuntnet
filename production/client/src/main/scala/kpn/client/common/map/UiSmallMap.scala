package kpn.client.common.map

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement

/*
  Small map that is shown in-line in page.
 */
object UiSmallMap {

  private case class Props(map: MapDefinition)

  private val component = ScalaComponent.builder[Props]("small-map")
    .render_P { props =>
      UiMap(UiMap.Styles.newSmallMap, props.map)
    }
    .build

  def apply(map: MapDefinition): VdomElement = component(Props(map))

}
