package kpn.api.common.node;

import kpn.api.common.NodeInfo;
import kpn.api.common.common.Reference;
import kpn.api.common.node.NodeIntegrity;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record NodeDetailsPage(
  NodeInfo nodeInfo,
  Boolean mixedRouteScopes,
  ImmutableList<Reference> routeReferences,
  ImmutableList<Reference> networkReferences,
  Optional<NodeIntegrity> integrity,
  Long changeCount
) {
}

/*
package kpn.api.common.node

import kpn.api.common.NodeInfo
import kpn.api.common.common.Reference

case class NodeDetailsPage(
  nodeInfo: NodeInfo,
  mixedRouteScopes: Boolean,
  routeReferences: Seq[Reference],
  networkReferences: Seq[Reference],
  integrity: Option[NodeIntegrity],
  changeCount: Long
)

*/
