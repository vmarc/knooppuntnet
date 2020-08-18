package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.common.ToStringBuilder
import kpn.api.common.data.Node

case class RouteNode(
  nodeType: RouteNodeType.Value = RouteNodeType.Start,
  node: Node = null,
  name: String = "",
  alternateName: String = "",
  definedInRelation: Boolean = false,
  definedInWay: Boolean = false
) {

  def id: Long = node.id

  def lat: String = node.latitude

  def lon: String = node.longitude

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("nodeType", nodeType).
    field("node", node).
    field("name", name).
    field("alternateName", alternateName).
    field("definedInRelation", definedInRelation).
    field("definedInWay", definedInWay).
    build

}
