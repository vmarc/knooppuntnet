package kpn.api.common

import kpn.api.custom.NetworkType

case class LocationChanges(
  networkType: NetworkType,
  locationNames: Seq[String],
  routeChanges: ChangeSetElementRefs,
  nodeChanges: ChangeSetElementRefs
)
