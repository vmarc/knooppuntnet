package kpn.server.analyzer.engine.tiles

import kpn.server.analyzer.engine.tiles.domain.Tile

trait TileFileRepository {

  def saveOrUpdate(tileType: String, tile: Tile, tileBytes: Array[Byte]): Unit

  def deleteTile(tileType: String, tile: Tile): Unit

  def existingTileNames(tileType: String, z: Int): Seq[String]

  def delete(tilenames: Seq[String]): Unit
}
