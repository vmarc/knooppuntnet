// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.shared.common.KnownElements
import kpn.shared.common.Ref

object UiNodeRef {

  private case class Props(context: Context, ref: Ref, knownElements: KnownElements)

  private val component = ScalaComponent.builder[Props]("node-ref")
    .render_P { props =>
      val ref = props.ref
      if (props.knownElements.nodeIds.contains(ref.id)) {
        props.context.gotoNode(ref.id, ref.name)
      }
      else {
        <.span(ref.name, " (", UiOsmLink.osmNode(ref.id), ")")
      }
    }
    .build

  def apply(ref: Ref, knownElements: KnownElements)(implicit context: Context): VdomElement = {
    component(Props(context, ref, knownElements))
  }
}
