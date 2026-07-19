package kpn.server.analyzer.engine.tiles.domain

import kpn.core.poi.PoiInfo

case class TilePois(tile: Tile, pois: Seq[PoiInfo])
