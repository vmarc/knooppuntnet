package kpn.api.common.monitor

import kpn.api.common.Bounds

case class MonitorRouteMapPage(
  relationId: Option[Long],
  routeName: String,
  routeDescription: String,
  groupName: String,
  groupDescription: String,
  bounds: Option[Bounds],
  prevSubRelation: Option [MonitorRouteSubRelation],
  nextSubRelation: Option[MonitorRouteSubRelation],
  osmSegments: Seq[MonitorRouteSegment],
  matchesGeoJson: Option[String],
  deviations: Seq[MonitorRouteDeviation],
  reference: Option[MonitorRouteReferenceInfo],
  subRelations: Seq[MonitorRouteSubRelation]
)
