package kpn.api.common.changes.details

import kpn.api.common.diff.RefDiffs
import kpn.api.custom.NetworkType

case class LocationChange(
  key: ChangeKey,
  networkType: NetworkType,
  locationName: String,
  nodes: RefDiffs,
  routes: RefDiffs,
  happy: Boolean,
  investigate: Boolean
) {
}
