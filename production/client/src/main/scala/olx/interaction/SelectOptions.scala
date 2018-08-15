package olx.interaction

import scala.scalajs.js

object SelectOptions {
  def apply(
    condition: ol.Ol.EventsConditionType,
    multi: Boolean,
    style: ol.style.Style /*| js.Array[ol.style.Style] | ol.StyleFunction*/
  ): SelectOptions = {
    js.Dynamic.literal(
      condition = condition,
      multi = multi,
      style = style
    ).asInstanceOf[SelectOptions]
  }
}

@js.native
trait SelectOptions extends js.Object {
  var condition: ol.Ol.EventsConditionType = js.native
  var style: ol.style.Style /*| js.Array[ol.style.Style] | ol.StyleFunction*/ = js.native
  var multi: Boolean = js.native
}
