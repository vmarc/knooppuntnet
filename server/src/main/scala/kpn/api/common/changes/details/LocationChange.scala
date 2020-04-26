package kpn.api.common.changes.details

import kpn.api.common.common.ToStringBuilder
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

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("key", key).
    field("networkType", networkType).
    field("locationName", locationName).
    field("nodes", nodes).
    field("routes", routes).
    build
}
