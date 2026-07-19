package kpn.api.common;

import kpn.api.common.NodeIntegrityCheck;
import kpn.api.common.RouteType;

import java.util.Optional;

public record NodeIntegrityCheckChange(
  RouteType routeType,
  Optional<NodeIntegrityCheck> before,
  Optional<NodeIntegrityCheck> after
) {
}

/*
package kpn.api.common

case class NodeIntegrityCheckChange(routeType: RouteType, before: Option[NodeIntegrityCheck], after: Option[NodeIntegrityCheck])

*/
