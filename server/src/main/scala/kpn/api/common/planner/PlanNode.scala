package kpn.api.common.planner

import kpn.api.common.LatLonImpl
import kpn.api.common.common.ToStringBuilder

case class PlanNode(
  featureId: String,
  nodeId: String,
  nodeName: String,
  coordinate: PlanCoordinate,
  latLon: LatLonImpl
) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("featureId", featureId).
    field("nodeId", nodeId).
    field("nodeName", nodeName).
    field("coordinate", coordinate).
    field("latLon", latLon).
    build

}
