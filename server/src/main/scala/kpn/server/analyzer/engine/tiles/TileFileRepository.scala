package kpn.server.analyzer.engine.tiles

import kpn.server.analyzer.engine.tiles.domain.OldTile

trait TileFileRepository {

  def saveOrUpdate(tileType: String, tile: OldTile, tileBytes: Array[Byte]): Unit

  def deleteTile(tileType: String, tile: OldTile): Unit

  def existingTileNames(tileType: String, z: Int): Seq[String]

  def delete(tilenames: Seq[String]): Unit
}
