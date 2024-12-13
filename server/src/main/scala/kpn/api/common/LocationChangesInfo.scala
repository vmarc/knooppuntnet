package kpn.api.common

case class LocationChangesInfo(
  networkType: NetworkType,
  locationInfos: Seq[LocationInfo],
  routeChanges: ChangeSetElementRefs,
  nodeChanges: ChangeSetElementRefs,
  happy: Boolean,
  investigate: Boolean
)
