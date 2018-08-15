package kpn.core.tiles.domain

case class TileRoute(routeId: Long, routeName: String, layer: String, segments: Seq[TileRouteSegment])
