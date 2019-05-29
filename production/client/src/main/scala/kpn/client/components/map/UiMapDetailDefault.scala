// TODO migrate to Angular
package kpn.client.components.map

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls

import scalacss.ScalaCssReact._

object UiMapDetailDefault {

  private case class Props(context: Context)

  private val component = ScalaComponent.builder[Props]("map-default")
    .render_P { props =>

      implicit val context: Context = props.context

      <.div(
        <.div(
          nls(
            "Click network node or route for more details, after zooming in sufficiently.",
            "Zoom in en klik op knooppunt of route voor verdere details."
          )
        ),
        <.div(
          UiMapDetail.Styles.note,
          <.div(
            nls(
              "The map contents is currently updated once every two hours.",
              "De kaart wordt momenteel om de twee uur automatisch aangepast aan de meest recente informatie in OpenStreetMap."
            )
          )
        )
      )
    }
    .build

  def apply()(implicit context: Context): VdomElement = component(Props(context))

}
