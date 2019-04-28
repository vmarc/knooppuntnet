package olx.tilegrid

import scala.scalajs.js

object XYZOptions {
  def apply(
    tileSize: Double,
    minZoom: Double,
    maxZoom: Double
  ): XYZOptions = {
    js.Dynamic.literal(
      tileSize = tileSize,
      minZoom = minZoom,
      maxZoom = maxZoom
    ).asInstanceOf[XYZOptions]
  }
}

@js.native
trait XYZOptions extends js.Object {
  var tileSize: Double = js.native
  var minZoom: Double = js.native
  var maxZoom: Double = js.native
}
