package kpn.api.common

import kpn.api.custom.NetworkType

case class ChangeSetLocation(
  networkType: NetworkType,
  locationName: String,
  routeChanges: ChangeSetElementRefs,
  nodeChanges: ChangeSetElementRefs,
  happy: Boolean,
  investigate: Boolean
)
