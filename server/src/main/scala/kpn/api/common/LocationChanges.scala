package kpn.api.common

case class LocationChanges(
  routeType: RouteType,
  locationNames: Seq[String],
  routeChanges: ChangeSetElementRefs,
  nodeChanges: ChangeSetElementRefs,
  happy: Boolean,
  investigate: Boolean
)
