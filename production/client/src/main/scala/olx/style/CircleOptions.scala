package olx.style

import scala.scalajs.js
import scala.scalajs.js.UndefOr

object CircleOptions {
  def apply(
    fill: UndefOr[ol.style.Fill] = js.undefined,
    radius: UndefOr[Double] = js.undefined,
    stroke: UndefOr[ol.style.Stroke] = js.undefined
  ): CircleOptions = {
    js.Dynamic.literal(
      fill = fill,
      radius = radius,
      stroke = stroke
    ).asInstanceOf[CircleOptions]
  }
}

@js.native
trait CircleOptions extends js.Object {
  var fill: UndefOr[ol.style.Fill] = js.native
  var radius: UndefOr[Double] = js.native
  var stroke: UndefOr[ol.style.Stroke] = js.native
}
