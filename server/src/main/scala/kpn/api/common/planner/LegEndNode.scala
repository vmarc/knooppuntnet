package kpn.api.common.planner

import kpn.api.common.common.ToStringBuilder

case class LegEndNode(nodeId: Long) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("nodeId", nodeId).
    build

}
