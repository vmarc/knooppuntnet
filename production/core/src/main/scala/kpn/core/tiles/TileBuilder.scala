package kpn.core.tiles

trait TileBuilder {
  def build(data: TileData): Array[Byte]
}
