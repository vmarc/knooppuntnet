package kpn.api.common.node;

import kpn.api.common.node.NodeIntegrityDetail;

import com.google.common.collect.ImmutableList;

public record NodeIntegrity(
  ImmutableList<NodeIntegrityDetail> details
) {}

/* TODO migrate

case class NodeIntegrity(details: Seq[NodeIntegrityDetail] = Seq.empty)

*/
