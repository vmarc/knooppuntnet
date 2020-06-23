package kpn.api.common.planner

import kpn.api.common.common.ToStringBuilder

case class RouteLegNode(nodeId: String, nodeName: String, lat: String, lon: String) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("nodeId", nodeId).
    field("nodeName", nodeName).
    field("lat", lat).
    field("lon", lon).
    build

}
