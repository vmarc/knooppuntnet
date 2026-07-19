package kpn.server.analyzer.engine.tile

object ZoomLevel {

  val poiTileMinZoom = 11
  val poiTileMaxZoom = 15

  val vectorTileMinZoom = 12
  val vectorTileMaxZoom = 13
  val vectorTileMaxOverZoom = 20

  val newMinZoom: Int = 2
  val newMaxZoom: Int = 13

  val minZoom: Int = 2
  val maxZoom: Int = 14

  val all: Seq[Int] = ZoomLevel.minZoom.to(ZoomLevel.maxZoom)

  val nodeMinZoom = 12

  val minZoomNodeNetwork = 6
  val minZoomNodeNetworkUserData = 11
  val minZoomNational = 7
  val minZoomRegional = 9
  val minZoomLocal = 11

  val minZoomOpenData = 6 // min(bitmapTileMinZoom, vectorTileMinZoom)
  val minZoomOpendataNode = 12
}
