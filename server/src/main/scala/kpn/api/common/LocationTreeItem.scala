package kpn.api.common

import kpn.api.custom.NetworkType

case class LocationTreeItem(
  level: Long,
  locationName: String,
  happy: Boolean,
  investigate: Boolean,
  networkType: Option[NetworkType], // only the root node will contain the networkType
  routeChanges: ChangeSetElementRefs, // empty for non-leaf nodes
  nodeChanges: ChangeSetElementRefs, // empty for non-leaf nodes
  expandable: Boolean,
  children: Seq[LocationTreeItem]
)
