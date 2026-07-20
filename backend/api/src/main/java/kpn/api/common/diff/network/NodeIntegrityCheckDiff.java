package kpn.api.common.diff.network;

import kpn.api.common.NodeIntegrityCheck;

import java.util.Optional;

public record NodeIntegrityCheckDiff(
  Optional<NodeIntegrityCheck> before,
  Optional<NodeIntegrityCheck> after
) {
}
