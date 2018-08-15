package olx

import scala.scalajs.js
import scala.scalajs.js.UndefOr

object ViewOptions {
  def apply(
    zoom: UndefOr[Double] = js.undefined,
    minZoom: UndefOr[Double] = js.undefined,
    maxZoom: UndefOr[Double] = js.undefined,
    center: UndefOr[ol.Coordinate] = js.undefined
  ): ViewOptions = {
    js.Dynamic.literal(
      zoom = zoom,
      minZoom = minZoom,
      maxZoom = maxZoom,
      center = center
    ).asInstanceOf[ViewOptions]
  }
}

@js.native
trait ViewOptions extends js.Object {
  var center: ol.Coordinate = js.native
  var maxZoom: Double = js.native
  var minZoom: Double = js.native
  var zoom: Double = js.native
}
