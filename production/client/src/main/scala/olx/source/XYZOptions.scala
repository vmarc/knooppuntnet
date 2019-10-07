package olx.source

import scala.scalajs.js
import scala.scalajs.js.UndefOr

object XYZOptions {
  def apply(
    minZoom: UndefOr[Double] = js.undefined,
    maxZoom: UndefOr[Double] = js.undefined,
    url: UndefOr[String] = js.undefined
  ): XYZOptions = {
    js.Dynamic.literal(
      minZoom = minZoom,
      maxZoom = maxZoom,
      url = url
    ).asInstanceOf[XYZOptions]
  }
}

@js.native
trait XYZOptions extends js.Object {
  var minZoom: Double = js.native
  var maxZoom: Double = js.native
  var url: UndefOr[String] = js.native
}
