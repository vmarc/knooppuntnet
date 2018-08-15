package olx.layer

import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scala.scalajs.js.|

object VectorTileOptions {
  def apply(
    renderMode: /*ol.layer.VectorTileRenderType |*/ String,
    source: ol.source.VectorTile,
    style: UndefOr[ol.Ol.StyleFunction] = js.undefined
  ): VectorTileOptions = {
    js.Dynamic.literal(
      renderMode = renderMode,
      source = source,
      style = style
    ).asInstanceOf[VectorTileOptions]
  }
}

@js.native
trait VectorTileOptions extends js.Object {
  var renderMode: /*ol.layer.VectorTileRenderType |*/ String = js.native
  var source: ol.source.VectorTile = js.native
  var style: ol.style.Style | js.Array[ol.style.Style] | ol.Ol.StyleFunction = js.native
}
