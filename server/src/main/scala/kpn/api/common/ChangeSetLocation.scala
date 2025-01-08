package kpn.api.common

case class ChangeSetLocation(
  routeType: RouteType,
  locationName: String,
  routeChanges: ChangeSetElementRefs,
  nodeChanges: ChangeSetElementRefs,
  happy: Boolean,
  investigate: Boolean
)
