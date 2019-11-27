package kpn.server.analyzer.engine.tile

trait TileUpdater {
  def update(minZoomLevel: Int): Unit
}
