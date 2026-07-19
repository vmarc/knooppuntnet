package kpn.api.common.node;

import kpn.api.common.node.NodeIntegrityDetail;

import com.google.common.collect.ImmutableList;

public record NodeIntegrity(
  ImmutableList<NodeIntegrityDetail> details
) {
}

/*
package kpn.api.common.node

case class NodeIntegrity(details: Seq[NodeIntegrityDetail] = Seq.empty)

*/
