package kpn.server.opendata.common

import kpn.server.analyzer.engine.tiles.domain.Tile

case class OpenDataTileRoutes(
  tile: Tile,
  routes: Seq[OpenDataRoute]
)
