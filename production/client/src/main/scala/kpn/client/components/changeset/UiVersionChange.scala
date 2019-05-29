// TODO migrate to Angular
package kpn.client.components.changeset

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiThin
import kpn.shared.data.Meta

object UiVersionChange {

  private case class Props(context: Context, before: Meta, after: Meta)

  private val component = ScalaComponent.builder[Props]("version-change")
    .render_P { props =>
      implicit val context: Context = props.context
      UiThin(
        if (props.before.version != props.after.version) {
          <.span(
            nls(
              s"Relation changed to v${props.after.version}.",
              s"Relatie veranderd naar v${props.after.version}."
            ),
            " ",
            UiMetaInfo(props.before)
          )
        } else {
          <.span(
            nls(
              "Relation not changed.",
              "Relatie niet gewijzigd."
            ),
            " ",
            UiMetaInfo(props.after)
          )
        }
      )
    }
    .build

  def apply(before: Meta, after: Meta)(implicit context: Context): VdomElement = {
    component(Props(context, before, after))
  }

}
