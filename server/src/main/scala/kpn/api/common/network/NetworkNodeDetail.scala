package kpn.api.common.network

import kpn.api.common.LatLon
import kpn.api.common.common.Ref
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

case class NetworkNodeDetail(
  id: Long,
  name: String,
  number: String,
  latitude: String,
  longitude: String,
  connection: Boolean, // true if all routes (in the network) that contain this node have role "connection" in the network relation
  roleConnection: Boolean,
  definedInRelation: Boolean,
  definedInRoute: Boolean,
  timestamp: Timestamp,
  lastSurvey: Option[Day],
  expectedRouteCount: String,
  routeReferences: Seq[Ref],
  facts: Seq[Fact],
  tags: Tags
) extends LatLon
