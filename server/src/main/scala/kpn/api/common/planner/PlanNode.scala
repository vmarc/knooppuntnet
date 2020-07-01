package kpn.api.common.planner

import kpn.api.common.LatLonImpl

case class PlanNode(
  featureId: String,
  nodeId: String,
  nodeName: String,
  coordinate: Array[Double],
  latLon: LatLonImpl
)
