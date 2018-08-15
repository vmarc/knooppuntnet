package kpn.core.tiles

import kpn.core.tiles.domain.Tile
import kpn.shared.NetworkType

trait TileRepository {

  def saveOrUpdate(networkType: NetworkType, tile: Tile, tileBytes: Array[Byte]): Unit

  def existingTileNames(networkType: NetworkType, z: Int): Seq[String]

  def delete(tilenames: Seq[String]): Unit
}
