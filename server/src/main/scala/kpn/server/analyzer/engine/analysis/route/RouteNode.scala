package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.data.Node

case class RouteNode(
  nodeType: RouteNodeType.Value = RouteNodeType.Start,
  node: Node = null,
  name: String = "",
  alternateName: String = "",
  longName: Option[String] = None,
  definedInRelation: Boolean = false,
  definedInWay: Boolean = false
) {

  def id: Long = node.id

  def lat: String = node.latitude

  def lon: String = node.longitude

  def missingInWays: Boolean = !definedInWay

}
