package kpn.api.common.node;

import kpn.api.common.Fact;
import kpn.api.common.RouteType;
import kpn.api.common.node.NodeNetworkIntegrityCheck;
import kpn.api.common.node.NodeNetworkRouteReference;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record NodeNetworkReference(
  RouteType routeType,
  Long networkId,
  String networkName,
  Boolean nodeDefinedInRelation,
  Boolean nodeConnection,
  Boolean nodeRoleConnection,
  Optional<NodeNetworkIntegrityCheck> nodeIntegrityCheck,
  ImmutableList<Fact> facts,
  ImmutableList<NodeNetworkRouteReference> routes
) {
}

/*
package kpn.api.common.node

import kpn.api.common.Fact
import kpn.api.common.RouteType

case class NodeNetworkReference(
  routeType: RouteType,
  networkId: Long,
  networkName: String,
  nodeDefinedInRelation: Boolean,
  nodeConnection: Boolean,
  nodeRoleConnection: Boolean,
  nodeIntegrityCheck: Option[NodeNetworkIntegrityCheck],
  facts: Seq[Fact],
  routes: Seq[NodeNetworkRouteReference]
)

*/
