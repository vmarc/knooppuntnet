package kpn.api.common.subset;

import kpn.api.common.OrphanNodeInfo;
import kpn.api.common.TimeInfo;
import kpn.api.common.subset.SubsetInfo;

import com.google.common.collect.ImmutableList;

public record SubsetOrphanNodesPage(
  TimeInfo timeInfo,
  SubsetInfo subsetInfo,
  ImmutableList<OrphanNodeInfo> nodes
) {
}

/*
package kpn.api.common.subset

import kpn.api.common.OrphanNodeInfo
import kpn.api.common.TimeInfo

case class SubsetOrphanNodesPage(
  timeInfo: TimeInfo,
  subsetInfo: SubsetInfo,
  nodes: Seq[OrphanNodeInfo]
)

*/
