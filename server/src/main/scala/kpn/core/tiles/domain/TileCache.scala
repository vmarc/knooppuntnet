package kpn.core.tiles.domain

class TileCache {

  private val tiles = scala.collection.mutable.Map[String, Tile]()

  def apply(z: Int, lon: Double, lat: Double): Tile = {
    val x = Tile.x(z, lon)
    val y = Tile.y(z, lat)
    apply(z, x, y)
  }

  def apply(z: Int, x: Int, y: Int): Tile = {
    val name = s"$z-$x-$y"
    tiles.get(name) match {
      case Some(tile) => tile
      case None =>
        val tile = new Tile(z, x, y)
        tiles(name) = tile
        tile
    }
  }
}
