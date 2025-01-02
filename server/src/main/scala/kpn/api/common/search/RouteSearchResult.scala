package kpn.api.common.search

import kpn.api.common.RouteScope

case class RouteSearchResult(
  id: Long,
  name: String,
  scopes: Seq[RouteScope],
  distance: Long,
  symbol: Option[String],
)
