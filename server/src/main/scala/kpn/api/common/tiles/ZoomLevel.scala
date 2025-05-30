package kpn.api.common.tiles

object ZoomLevel {

  val bitmapTileMinZoom = 6
  val bitmapTileMaxZoom = 11

  val poiTileMinZoom = 11
  val poiTileMaxZoom = 15

  val vectorTileMinZoom = 12
  val vectorTileMaxZoom = 14
  val vectorTileMaxOverZoom = 20

  val newMinZoom: Int = 2
  val newMaxZoom: Int = 13

  val minZoom: Int = 2 // min(bitmapTileMinZoom, vectorTileMinZoom)
  val maxZoom: Int = 14 //max(bitmapTileMaxZoom, vectorTileMaxZoom)

  val all: Seq[Int] = ZoomLevel.minZoom.to(ZoomLevel.maxZoom)

  val nodeMinZoom = 12
}
