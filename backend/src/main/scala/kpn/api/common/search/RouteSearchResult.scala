package kpn.api.common.search

import kpn.api.common.Bounds
import kpn.api.common.RouteScope

case class RouteSearchResult(
  id: Long,
  name: String,
  scopes: Seq[RouteScope],
  distance: Long,
  symbol: Option[String],
  bounds: Option[Bounds],
  routeIds: Seq[Long],
) {
  def toRouteListItem: RouteListItem = {
    RouteListItem(
      id,
      name,
      distance,
      symbol,
      bounds,
      routeIds,
    )
  }
}
