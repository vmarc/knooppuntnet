package kpn.api.common.search

import kpn.api.common.Bounds

case class RouteListItem(
  id: Long,
  name: String,
  distance: Long,
  symbol: Option[String],
  bounds: Option[Bounds],
  routeIds: Seq[Long],
)
