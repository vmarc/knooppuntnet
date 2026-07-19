package kpn.api.common;

import kpn.api.common.NodeIntegrityCheck;

import com.google.common.collect.ImmutableList;

public record NetworkIntegrityCheckFailed(
  Long count,
  ImmutableList<NodeIntegrityCheck> checks
) {
}

/*
package kpn.api.common

case class NetworkIntegrityCheckFailed(count: Long, checks: Seq[NodeIntegrityCheck])

*/
