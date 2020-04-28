package kpn.api.common

import kpn.api.common.common.ToStringBuilder

case class LocationChangesTreeNode(
  locationName: String,
  routeChanges: ChangeSetElementRefs, // empty for non-leaf nodes
  nodeChanges: ChangeSetElementRefs, // empty for non-leaf nodes
  children: Seq[LocationChangesTreeNode], // empty for leaf nodes
  happy: Boolean,
  investigate: Boolean
) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("locationName", locationName).
    field("routeChanges", routeChanges).
    field("nodeChanges", nodeChanges).
    optionalCollection("children", children).
    field("happy", happy).
    field("investigate", investigate).
    build
}
