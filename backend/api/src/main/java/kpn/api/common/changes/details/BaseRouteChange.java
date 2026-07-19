package kpn.api.common.changes.details;

import kpn.api.common.ChangeType;
import kpn.api.common.changes.details.ChangeKey;
import kpn.api.common.data.MetaData;
import kpn.api.common.diff.WayDiffsInfo;
import kpn.api.common.diff.route.RouteDiff;
import kpn.api.common.route.GeometryDiff;

import java.util.Optional;

public record BaseRouteChange(
  String _id,
  ChangeKey key,
  ChangeType changeType,
  Optional<MetaData> before,
  Optional<MetaData> after,
  RouteDiff routeDiff,
  Optional<WayDiffsInfo> wayDiffs,
  Optional<GeometryDiff> geometryDiff
) {
}

/*
package kpn.api.common.changes.details

import kpn.api.common.ChangeType
import kpn.api.common.data.MetaData
import kpn.api.common.diff.WayDiffsInfo
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.route.GeometryDiff
import kpn.core.doc.WithStringId

case class BaseRouteChange(
  _id: String,
  key: ChangeKey,
  changeType: ChangeType,
  before: Option[MetaData],
  after: Option[MetaData],
  routeDiff: RouteDiff,
  wayDiffs: Option[WayDiffsInfo],
  geometryDiff: Option[GeometryDiff]
) extends WithStringId {

  def routeId: Long = key.elementId
}

*/
