package kpn.api.common

import kpn.api.common.common.ToStringBuilder
import kpn.api.custom.NetworkType

case class LocationChangesTree(
  networkType: NetworkType,
  locationName: String,
  happy: Boolean,
  investigate: Boolean,
  children: Seq[LocationChangesTreeNode]
) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("networkType", networkType).
    field("locationName", locationName).
    field("happy", happy).
    field("investigate", investigate).
    optionalCollection("children", children).
    build
}

