package kpn.api.common;

import kpn.api.common.ChangeSetElementRefs;

import com.google.common.collect.ImmutableList;

public record LocationChangesTreeNode(
  String locationName,
  ChangeSetElementRefs routeChanges,
  ChangeSetElementRefs nodeChanges,
  ImmutableList<LocationChangesTreeNode> children,
  Boolean happy,
  Boolean investigate
) {
}

/*
package kpn.api.common

case class LocationChangesTreeNode(
  locationName: String,
  routeChanges: ChangeSetElementRefs, // empty for non-leaf nodes
  nodeChanges: ChangeSetElementRefs, // empty for non-leaf nodes
  children: Seq[LocationChangesTreeNode], // empty for leaf nodes
  happy: Boolean,
  investigate: Boolean
)

*/
