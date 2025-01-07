package kpn.api.common.search

case class RouteList(
  international: Option[Seq[RouteListItem]],
  national: Option[Seq[RouteListItem]],
  regional: Option[Seq[RouteListItem]],
  local: Option[Seq[RouteListItem]],
  unknown: Option[Seq[RouteListItem]],
  size: Long,
)
