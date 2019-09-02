// TODO migrate to Angular
package kpn.client.components.common

import chandu0101.scalajs.react.components.materialui.Mui.Styles.colors.grey800
import chandu0101.scalajs.react.components.materialui.Mui.SvgIcons.MapsDirectionsBike
import chandu0101.scalajs.react.components.materialui.Mui.SvgIcons.MapsDirectionsBoat
import chandu0101.scalajs.react.components.materialui.Mui.SvgIcons.MapsDirectionsWalk
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
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
      marginBottom(3.px),
      display.flex,
      alignItems.center
    )

    val text: StyleA = style(
      paddingLeft(10.px)
    )

    val connection: StyleA = style(
      paddingLeft(10.px)
    )
  }

  private case class Props(context: Context, networkType: NetworkType, text: VdomElement, connection: Boolean)

  private val component = ScalaComponent.builder[Props]("network-type")
    .render_P { props =>
      implicit val context: Context = props.context
      <.div(
        Styles.wrapper,
        <.div(
          props.networkType match {
            case NetworkType.hiking => UiIcon(MapsDirectionsWalk, grey800)
            case NetworkType.bicycle => UiIcon(MapsDirectionsBike, grey800)
            case NetworkType.horseRiding =>
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
        ),
        TagMod.when(props.connection) {
          <.div(
            Styles.connection,
            UiImage("link.png")
          )
        }
      )
    }
    .build

  def apply(networkType: NetworkType, text: VdomElement, connection: Boolean = false)(implicit context: Context): VdomElement = {
    component(Props(context, networkType, text, connection))
  }
}
