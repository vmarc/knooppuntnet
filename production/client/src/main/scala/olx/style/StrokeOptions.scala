package olx.style

import scala.scalajs.js
import scala.scalajs.js.|

object StrokeOptions {
  def apply(
    color: ol.Color,
    width: Double,
    lineDash: js.Array[Int]
  ): StrokeOptions = {
    js.Dynamic.literal(
      color = color.asInstanceOf[js.Any],
      width = width,
      lineDash = lineDash
    ).asInstanceOf[StrokeOptions]
  }
}

@js.native
trait StrokeOptions extends js.Object {
  var color: ol.Color | String = js.native
  var width: Double = js.native
  var lineDash: js.Array[Int] = js.native
}
