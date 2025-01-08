package kpn.api.common

case class LocationChangesTree(
  routeType: RouteType,
  locationName: String,
  happy: Boolean,
  investigate: Boolean,
  children: Seq[LocationChangesTreeNode]
)
