package kpn.api.common.planner

import kpn.api.common.LatLonImpl
import kpn.api.common.common.ToStringBuilder

case class PlanNode(
  featureId: String,  // TODO PLAN has become obsolete ??? remove ???
  nodeId: String,
  nodeName: String,
  coordinate: PlanCoordinate,
  latLon: LatLonImpl  // TODO PLAN has become obsolete? NO: used to create GPX on client ==> YES: zou lokaal terug van Coordinate berekend kunnen worden...
) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("featureId", featureId).
    field("nodeId", nodeId).
    field("nodeName", nodeName).
    field("coordinate", coordinate).
    field("latLon", latLon).
    build

}
