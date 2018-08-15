package olx.layer

import scala.scalajs.js

object TileOptions {
  def apply(source: ol.source.Tile): TileOptions =
    js.Dynamic.literal(source = source).asInstanceOf[TileOptions]
}

@js.native
trait TileOptions extends js.Object {
  var source: ol.source.Tile = js.native
}
