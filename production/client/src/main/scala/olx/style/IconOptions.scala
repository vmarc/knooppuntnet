package olx.style

import scala.scalajs.js

object IconOptions {
  def apply(
    anchor: js.Array[Double],
    anchorXUnits: String, // ol.style.IconAnchorUnits,
    anchorYUnits: String, // ol.style.IconAnchorUnits,
    src: String
  ): IconOptions =
    js.Dynamic.literal(
      anchor = anchor,
      anchorXUnits = anchorXUnits,
      anchorYUnits = anchorYUnits,
      src = src
    ).asInstanceOf[IconOptions]
}

@js.native
trait IconOptions extends js.Object {
  var anchor: js.Array[Double] = js.native
  var anchorXUnits: String /*ol.style.types.IconAnchorUnits*/ = js.native
  var anchorYUnits: String /*ol.style.types.IconAnchorUnits*/ = js.native
  var src: String = js.native
}
