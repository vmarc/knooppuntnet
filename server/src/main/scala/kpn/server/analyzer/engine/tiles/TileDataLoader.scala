package kpn.server.analyzer.engine.tiles

import kpn.api.common.RouteType

trait TileDataLoader {

  def load(routeType: RouteType, nodenetwork: Boolean): TileData
}
