package kpn.server.tv

import kpn.server.analyzer.engine.tiles.domain.Tile

case class TvTileNodes(tile: Tile, nodes: Seq[TvNode])
