package kpn.api.common

case class LocationChangesTreeNode(
  locationName: String,
  routeChanges: ChangeSetElementRefs, // empty for non-leaf nodes
  nodeChanges: ChangeSetElementRefs, // empty for non-leaf nodes
  children: Seq[LocationChangesTreeNode], // empty for leaf nodes
  happy: Boolean,
  investigate: Boolean
)
