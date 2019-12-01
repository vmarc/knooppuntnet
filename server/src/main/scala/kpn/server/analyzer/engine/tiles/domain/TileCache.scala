package kpn.server.analyzer.engine.tiles.domain

class TileCache {

  private val tiles = scala.collection.mutable.Map[String, Tile]()

  def apply(z: Int, lon: Double, lat: Double): Tile = {
    val x = Tile.x(z, lon)
    val y = Tile.y(z, lat)
    apply(z, x, y)
  }

  def apply(z: Int, x: Int, y: Int): Tile = {
    val name = s"$z-$x-$y"
    getTile(name, z, x, y)
  }

  def apply(tileName: String): Tile = {
    val splitted = tileName.split("-")
    val z = splitted(0).toInt
    val x = splitted(1).toInt
    val y = splitted(2).toInt
    getTile(tileName, z, x, y)
  }

  private def getTile(tileName: String, z: Int, x: Int, y: Int): Tile = {
    tiles.getOrElseUpdate(tileName, new Tile(z, x, y))
  }
}
