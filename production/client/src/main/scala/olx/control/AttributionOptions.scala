package olx.control

import org.scalajs.dom._

import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scala.scalajs.js.|

object AttributionOptions {
  def apply(
    collapsible: UndefOr[Boolean] = js.undefined
  ): AttributionOptions =
    js.Dynamic.literal(
      collapsible = collapsible
    ).asInstanceOf[AttributionOptions]
}

@js.native
trait AttributionOptions extends js.Object {
  var className: String = js.native
  var target: Element = js.native
  var collapsible: Boolean = js.native
  var collapsed: Boolean = js.native
  var tipLabel: String = js.native
  var label: String | Node = js.native
  var collapseLabel: String | Node = js.native
  var render: js.Function1[ol.MapEvent, Any] = js.native
}
