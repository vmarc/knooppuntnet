package kpn.server.analyzer.engine.tiles.domain

class TileCache {

  private val tiles = scala.collection.mutable.Map[String, Tile]()

  def tileContainingWorldCoordinate(z: Int, worldX: Double, worldY: Double): Tile = {
    val x = Tile.tileX(z, worldX)
    val y = Tile.tileY(z, worldY)
    apply(z, x, y)
  }

  def apply(z: Int, x: Int, y: Int): Tile = {
    val name = s"$z-$x-$y"
    cachedOrNewTile(name, z, x, y)
  }

  def apply(tileName: String): Tile = {
    val splitted = tileName.split("-")
    val z = splitted(0).toInt
    val x = splitted(1).toInt
    val y = splitted(2).toInt
    cachedOrNewTile(tileName, z, x, y)
  }

  private def cachedOrNewTile(tileName: String, z: Int, x: Int, y: Int): Tile = {
    tiles.getOrElseUpdate(tileName, new Tile(z, x, y))
  }
}
