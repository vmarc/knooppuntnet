package olx.source

import scala.scalajs.js
import scala.scalajs.js.UndefOr

object VectorTileOptions {
  def apply(
    format: ol.format.Feature,
    tileGrid: ol.tilegrid.TileGrid,
    tilePixelRatio: UndefOr[Double] = js.undefined,
    tileUrlFunction: UndefOr[ol.Ol.TileUrlFunctionType] = js.undefined,
    url: UndefOr[String] = js.undefined
  ): VectorTileOptions = {
    js.Dynamic.literal(
      format = format,
      tileGrid = tileGrid,
      tilePixelRatio = tilePixelRatio,
      tileUrlFunction = tileUrlFunction,
      url = url
    ).asInstanceOf[VectorTileOptions]
  }
}

@js.native
trait VectorTileOptions extends js.Object {
  var format: ol.format.Feature = js.native
  var tileGrid: ol.tilegrid.TileGrid = js.native
  var tilePixelRatio: Double = js.native
  var tileUrlFunction: UndefOr[ol.Ol.TileUrlFunctionType] = js.native
  var url: UndefOr[String] = js.native
}
