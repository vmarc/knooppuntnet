package kpn.server.analyzer.engine.tiles

trait TileBuilder {
  def build(data: TileData): Array[Byte]
}
