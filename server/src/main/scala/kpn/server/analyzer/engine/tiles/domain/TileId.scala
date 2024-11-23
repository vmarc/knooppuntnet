package kpn.server.analyzer.engine.tiles.domain

case class TileId(z: Long, x: Long, y: Long) {
  def name: String = {
    s"$z-$x-$y"
  }
}
