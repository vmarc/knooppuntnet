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
