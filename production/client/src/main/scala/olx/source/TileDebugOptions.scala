package olx.source

import scala.scalajs.js

object TileDebugOptions {
  def apply(
    projection: ol.ProjectionLike,
    tileGrid: ol.tilegrid.TileGrid
  ): TileDebugOptions = {
    js.Dynamic.literal(
      projection = projection,
      tileGrid = tileGrid //,
      // wrapX = wrapX
    ).asInstanceOf[TileDebugOptions]
  }
}

@js.native
trait TileDebugOptions extends js.Object {
  var projection: ol.ProjectionLike = js.native
  var tileGrid: ol.tilegrid.TileGrid = js.native
  var wrapX: Boolean = js.native
}
