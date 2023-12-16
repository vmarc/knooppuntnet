package kpn.server.analyzer.engine.tiles.domain

class OldTileCache {

  private val tiles = scala.collection.mutable.Map[String, OldTile]()

  def apply(z: Int, lon: Double, lat: Double): OldTile = {
    val x = OldTile.x(z, lon)
    val y = OldTile.y(z, lat)
    apply(z, x, y)
  }

  def apply(z: Int, x: Int, y: Int): OldTile = {
    val name = s"$z-$x-$y"
    getTile(name, z, x, y)
  }

  def apply(tileName: String): OldTile = {
    val splitted = tileName.split("-")
    val z = splitted(0).toInt
    val x = splitted(1).toInt
    val y = splitted(2).toInt
    getTile(tileName, z, x, y)
  }

  private def getTile(tileName: String, z: Int, x: Int, y: Int): OldTile = {
    tiles.getOrElseUpdate(tileName, OldTile(z, x, y))
  }
}
