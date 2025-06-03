package kpn.server.analyzer.engine.tiles.domain

case class TileId(z: Int, x: Int, y: Int) {
  def name: String = {
    s"$z-$x-$y"
  }
}
