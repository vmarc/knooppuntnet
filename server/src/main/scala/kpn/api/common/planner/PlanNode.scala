package kpn.api.common.planner

import kpn.api.common.LatLonImpl

case class PlanNode(
  featureId: String, // TODO PLAN has become obsolete ??? remove ???
  nodeId: String,
  nodeName: String,
  coordinate: PlanCoordinate,
  latLon: LatLonImpl // TODO PLAN has become obsolete? NO: used to create GPX on client ==> YES: zou lokaal terug van Coordinate berekend kunnen worden...
) {
}
