// Migrated to Angular: network-type-icon.component.ts
package kpn.client.components.common

import chandu0101.scalajs.react.components.materialui.Mui.Styles.colors.grey800
import chandu0101.scalajs.react.components.materialui.Mui.SvgIcons.MapsDirectionsBike
import chandu0101.scalajs.react.components.materialui.Mui.SvgIcons.MapsDirectionsBoat
import chandu0101.scalajs.react.components.materialui.Mui.SvgIcons.MapsDirectionsWalk
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.components.home.UiIcon
import kpn.shared.NetworkType

object UiNetworkTypeIcon {

  private case class Props(networkType: NetworkType)

  private val component = ScalaComponent.builder[Props]("network-type")
    .render_P { props =>
      props.networkType match {
        case NetworkType.hiking => UiIcon(MapsDirectionsWalk, grey800)
        case NetworkType.bicycle => UiIcon(MapsDirectionsBike, grey800)
        case NetworkType.horse => // UiIcon(NotificationAirlineSeatFlatAngled, grey800, style1)
          <.img(
            ^.src := "/assets/images/horse.svg",
            ^.width := "24px",
            ^.height := "24px"
          )
        case NetworkType.motorboat => UiIcon(MapsDirectionsBoat, grey800)
        case NetworkType.canoe =>
          <.img(
            ^.src := "/assets/images/canoe.svg",
            ^.width := "24px",
            ^.height := "24px"
          )
        case NetworkType.inlineSkates =>
          <.img(
            ^.src := "/assets/images/inlineSkates.svg",
            ^.width := "24px",
            ^.height := "24px"
          )
      }
    }
    .build

  def apply(networkType: NetworkType): VdomElement = component(Props(networkType))
}
