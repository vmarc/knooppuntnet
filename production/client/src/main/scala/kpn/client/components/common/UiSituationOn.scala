// Migrated to Angular: situation-on.component.ts
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.shared.Timestamp

object UiSituationOn {

  private case class Props(context: Context, situationOn: Option[Timestamp])

  private val component = ScalaComponent.builder[Props]("situation-on")
    .render_P { props =>
      implicit val context: Context = props.context
      <.div(
        TagMod.when(props.situationOn.isDefined) {
          <.div(
            nls("Situation on", "Situatie op"),
            ": ",
            props.situationOn.get.yyyymmddhhmm
          )
        }
      )
    }
    .build

  def apply[T](situationOn: Option[Timestamp])(implicit context: Context): TagMod = {
    component(Props(context, situationOn))
  }
}
