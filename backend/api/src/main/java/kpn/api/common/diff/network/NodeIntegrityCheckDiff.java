package kpn.api.common.diff.network;

import kpn.api.common.NodeIntegrityCheck;

import java.util.Optional;

public record NodeIntegrityCheckDiff(
  Optional<NodeIntegrityCheck> before,
  Optional<NodeIntegrityCheck> after
) {
}

/*
package kpn.api.common.diff.network

import kpn.api.common.NodeIntegrityCheck

case class NodeIntegrityCheckDiff(before: Option[NodeIntegrityCheck], after: Option[NodeIntegrityCheck])

*/
