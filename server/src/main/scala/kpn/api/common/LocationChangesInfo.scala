package kpn.api.common

case class LocationChangesInfo(
  routeType: RouteType,
  locationInfos: Seq[LocationInfo],
  routeChanges: ChangeSetElementRefs,
  nodeChanges: ChangeSetElementRefs,
  happy: Boolean,
  investigate: Boolean
)
