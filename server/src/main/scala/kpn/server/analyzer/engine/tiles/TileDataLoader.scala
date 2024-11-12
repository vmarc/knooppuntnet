package kpn.server.analyzer.engine.tiles

import kpn.api.custom.NetworkType

trait TileDataLoader {

  def load(networkType: NetworkType): TileData
}
