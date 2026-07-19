package kpn.api.common.node;

import kpn.api.common.changes.filter.ChangesFilterOption;
import kpn.api.common.node.NodeChangeInfo;

import com.google.common.collect.ImmutableList;

public record NodeChangesPage(
  Long nodeId,
  String nodeName,
  ImmutableList<ChangesFilterOption> filterOptions,
  ImmutableList<NodeChangeInfo> changes,
  Long totalCount,
  Long changeCount
) {
}

/*
package kpn.api.common.node

import kpn.api.common.changes.filter.ChangesFilterOption

case class NodeChangesPage(
  nodeId: Long,
  nodeName: String,
  filterOptions: Seq[ChangesFilterOption],
  changes: Seq[NodeChangeInfo],
  totalCount: Long,
  changeCount: Long
)

*/
