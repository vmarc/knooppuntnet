package kpn.server.analyzer.engine.tiles

import kpn.api.common.NetworkType

trait TileDataLoader {

  def load(networkType: NetworkType, nodenetwork: Boolean): TileData
}
