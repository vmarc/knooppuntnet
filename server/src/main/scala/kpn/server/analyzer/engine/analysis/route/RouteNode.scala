package kpn.server.analyzer.engine.analysis.route

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

  override def toString: String = {
    val clazz = getClass.getSimpleName
    val details = new RouteNodeFormatter(this).shortString
    s"$clazz($details)"
  }

}
