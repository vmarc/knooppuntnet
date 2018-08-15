package kpn.shared.tiles

import scala.math.min

object ZoomLevel {

  val bitmapTileMinZoom = 6
  val bitmapTileMaxZoom = 11

  val vectorTileMinZoom = 12
  val vectorTileMaxZoom = 15
  val vectorTileMaxOverZoom = 20
  val vectorTileFullDetailZoom = 14

  val minZoom: Int = min(bitmapTileMinZoom, vectorTileMinZoom)
  val maxZoom: Int = min(bitmapTileMaxZoom, vectorTileMaxZoom)

}
