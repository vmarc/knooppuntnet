package kpn.api.common.route;

import kpn.api.common.ChangeType;
import kpn.api.common.changes.ChangeSetInfo;
import kpn.api.common.changes.details.ChangeKey;
import kpn.api.common.data.MetaData;
import kpn.api.common.diff.WayDiffsInfo;
import kpn.api.common.diff.route.RouteDiff;
import kpn.api.common.route.GeometryDiff;
import kpn.api.common.route.RouteNode;
import kpn.api.common.route.RouteNodeChange;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record RouteChangeInfo(
  Long rowIndex,
  Long id,
  Long version,
  ChangeKey changeKey,
  ChangeType changeType,
  Optional<String> comment,
  Optional<MetaData> before,
  Optional<MetaData> after,
  Optional<RouteDiff> diffs,
  ImmutableList<RouteNode> nodes,
  ImmutableList<RouteNodeChange> nodeChanges,
  Optional<ChangeSetInfo> changeSetInfo,
  Optional<WayDiffsInfo> wayDiffs,
  Optional<GeometryDiff> geometryDiff,
  Boolean happy,
  Boolean investigate
) {
}

/*
package kpn.api.common.route

import kpn.api.common.ChangeType
import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.data.MetaData
import kpn.api.common.diff.WayDiffsInfo
import kpn.api.common.diff.route.RouteDiff

case class RouteChangeInfo(
  rowIndex: Long,
  id: Long,
  version: Long,
  changeKey: ChangeKey,
  changeType: ChangeType,
  comment: Option[String],
  before: Option[MetaData],
  after: Option[MetaData],
  diffs: Option[RouteDiff],
  nodes: Seq[RouteNode],
  nodeChanges: Seq[RouteNodeChange],
  changeSetInfo: Option[ChangeSetInfo],
  wayDiffs: Option[WayDiffsInfo],
  geometryDiff: Option[GeometryDiff],
  happy: Boolean,
  investigate: Boolean
)

*/
