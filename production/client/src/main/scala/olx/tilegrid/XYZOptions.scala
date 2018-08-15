package olx.tilegrid

import scala.scalajs.js

object XYZOptions {
  def apply(
    minZoom: Double,
    maxZoom: Double
  ): XYZOptions = {
    js.Dynamic.literal(
      minZoom = minZoom,
      maxZoom = maxZoom
    ).asInstanceOf[XYZOptions]
  }
}

@js.native
trait XYZOptions extends js.Object {
  var minZoom: Double = js.native
  var maxZoom: Double = js.native
}
