package kpn.server.analyzer.engine.tiles.domain

case class TileRouteSegment(pathId: Long, oneWay: Boolean, surface: String, lines: Seq[Line])
