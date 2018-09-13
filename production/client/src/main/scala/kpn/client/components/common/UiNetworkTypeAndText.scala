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
import kpn.client.common.Context
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.home.UiIcon
import kpn.shared.NetworkType
import scalacss.ScalaCssReact._

object UiNetworkTypeAndText {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val wrapper: StyleA = style(
      marginBottom(3.px)
    )

    val icon: StyleA = style(
      display.inlineBlock
    )

    val text: StyleA = style(
      display.inlineBlock,
      verticalAlign.top,
      paddingLeft(10.px),
      lineHeight(24.px)
    )
  }

  private case class Props(context: Context, networkType: NetworkType, text: VdomElement)

  private val component = ScalaComponent.builder[Props]("network-type")
    .render_P { props =>
      implicit val context: Context = props.context
      <.div(
        Styles.wrapper,
        <.div(
          Styles.icon,
          props.networkType match {
            case NetworkType.hiking => UiIcon(MapsDirectionsWalk, grey800)
            case NetworkType.bicycle => UiIcon(MapsDirectionsBike, grey800)
            case NetworkType.horse =>
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
        ),
        <.div(
          Styles.text,
          props.text
        )
      )
    }
    .build

  def apply(networkType: NetworkType, text: VdomElement)(implicit context: Context): VdomElement = {
    component(Props(context, networkType, text))
  }
}
