package kpn.api.common

case class LocationChangesTree(
  networkType: NetworkType,
  locationName: String,
  happy: Boolean,
  investigate: Boolean,
  children: Seq[LocationChangesTreeNode]
)
