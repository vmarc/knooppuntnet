package kpn.api.common

case class ChangeSetLocation(
  networkType: NetworkType,
  locationName: String,
  routeChanges: ChangeSetElementRefs,
  nodeChanges: ChangeSetElementRefs,
  happy: Boolean,
  investigate: Boolean
)
