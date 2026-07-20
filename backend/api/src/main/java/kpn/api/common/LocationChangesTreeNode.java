package kpn.api.common;

import kpn.api.common.ChangeSetElementRefs;

import com.google.common.collect.ImmutableList;

public record LocationChangesTreeNode(
  String locationName,
  ChangeSetElementRefs routeChanges, // empty for non-leaf nodes
  ChangeSetElementRefs nodeChanges, // empty for non-leaf nodes
  ImmutableList<LocationChangesTreeNode> children, // empty for non-leaf nodes
  Boolean happy,
  Boolean investigate
) {
}
