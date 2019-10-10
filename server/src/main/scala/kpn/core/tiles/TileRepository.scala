package kpn.core.tiles

import kpn.core.tiles.domain.Tile

trait TileRepository {

  def saveOrUpdate(tileType: String, tile: Tile, tileBytes: Array[Byte]): Unit

  def existingTileNames(tileType: String, z: Int): Seq[String]

  def delete(tilenames: Seq[String]): Unit
}
