package kpn.server.analyzer.engine.tiles.domain

import scala.collection.concurrent.TrieMap

class TileCache(tiles: Tiles) {

  private val tileMap = TrieMap.empty[String, Tile]

  def apply(tileName: String): Tile = {
    cachedOrNewTile(tileName)
  }

  private def cachedOrNewTile(tileName: String): Tile = {
    tileMap.getOrElseUpdate(
      tileName,
      {
        val tileId = TileId(tileName)
        tiles.tile(tileId)
      }
    )
  }
}
