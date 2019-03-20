package kpn.core.planner.plan

import kpn.core.directions.Latlon

case class PlanNode(nodeId: Long, nodeName: String, latlon: Latlon)
