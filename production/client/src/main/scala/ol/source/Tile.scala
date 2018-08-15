package ol.source

import scala.scalajs.js
import scala.scalajs.js.annotation._

@js.native
@JSGlobal("ol.source.Tile")
class Tile protected () extends ol.source.Source {
  def getTileGrid(): ol.tilegrid.TileGrid = js.native
}
