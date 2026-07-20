package kpn.api.common;

import kpn.api.common.NodeIntegrityCheck;

import com.google.common.collect.ImmutableList;

public record NetworkIntegrityCheckFailed(
  Long count,
  ImmutableList<NodeIntegrityCheck> checks
) {
}
