package kpn.server.tv

import kpn.server.analyzer.engine.tiles.domain.Tile

case class TvTileRoutes(tile: Tile, routes: Seq[TvRoute])
