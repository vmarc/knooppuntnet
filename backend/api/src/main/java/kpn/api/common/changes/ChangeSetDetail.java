package kpn.api.common.changes;

import kpn.api.common.ChangeSetSubsetElementRefs;
import kpn.api.common.ChangeSetSummary;
import kpn.api.common.changes.ChangeSetInfo;
import kpn.api.common.changes.details.NetworkChangeInfo;
import kpn.api.common.common.KnownElements;
import kpn.api.common.node.NodeChangeInfo;
import kpn.api.common.route.RouteChangeInfo;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record ChangeSetDetail(
  ChangeSetSummary summary,
  Optional<ChangeSetInfo> changeSetInfo,
  ImmutableList<NetworkChangeInfo> networkChanges,
  ImmutableList<ChangeSetSubsetElementRefs> orphanRouteChanges,
  ImmutableList<ChangeSetSubsetElementRefs> orphanNodeChanges,
  ImmutableList<RouteChangeInfo> routeChanges,
  ImmutableList<NodeChangeInfo> nodeChanges,
  KnownElements knownElements
) {
}

/*
package kpn.api.common.changes

import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeSetSummary
import kpn.api.common.changes.details.NetworkChangeInfo
import kpn.api.common.common.KnownElements
import kpn.api.common.node.NodeChangeInfo
import kpn.api.common.route.RouteChangeInfo

case class ChangeSetDetail(
  summary: ChangeSetSummary,
  changeSetInfo: Option[ChangeSetInfo],
  networkChanges: Seq[NetworkChangeInfo],
  orphanRouteChanges: Seq[ChangeSetSubsetElementRefs],
  orphanNodeChanges: Seq[ChangeSetSubsetElementRefs],
  routeChanges: Seq[RouteChangeInfo],
  nodeChanges: Seq[NodeChangeInfo],
  knownElements: KnownElements
)

*/
