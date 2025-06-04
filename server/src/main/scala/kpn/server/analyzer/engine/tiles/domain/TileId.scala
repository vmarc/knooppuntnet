package kpn.server.analyzer.engine.tiles.domain

object TileId {
  def apply(name: String): TileId = {
    val splitted = name.split("-")
    TileId(splitted(0).toInt, splitted(1).toInt, splitted(2).toInt)
  }
}

case class TileId(z: Int, x: Int, y: Int) {
  def name: String = {
    s"$z-$x-$y"
  }
}
