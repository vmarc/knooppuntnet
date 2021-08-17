package kpn.api.common

import kpn.api.custom.NetworkType

case class LocationChangesTree(
  networkType: NetworkType,
  locationName: String,
  happy: Boolean,
  investigate: Boolean,
  children: Seq[LocationChangesTreeNode]
)
