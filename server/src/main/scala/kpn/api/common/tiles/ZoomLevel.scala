package kpn.api.common.tiles

import scala.math.max
import scala.math.min

object ZoomLevel {

  val bitmapTileMinZoom = 6
  val bitmapTileMaxZoom = 11

  val poiTileMinZoom = 11
  val poiTileMaxZoom = 15

  val vectorTileMinZoom = 12
  val vectorTileMaxZoom = 14
  val vectorTileMaxOverZoom = 20

  val minZoom: Int = min(bitmapTileMinZoom, vectorTileMinZoom)
  val maxZoom: Int = max(bitmapTileMaxZoom, vectorTileMaxZoom)

  val all: Seq[Int] = ZoomLevel.minZoom.to(ZoomLevel.maxZoom)

  val nodeMinZoom = 9

}
