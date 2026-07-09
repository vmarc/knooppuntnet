package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.common.common.Ref
import kpn.api.common.data.Node
import kpn.api.common.route.RouteNode

case class RouteNodeAnalysis(
  node: Node,
  name: String,
  alternateName: String,
  longName: Option[String],
  isInWay: Boolean,
) {
  def toRouteNode: RouteNode = {
    RouteNode(
      node.id,
      node.latitude,
      node.longitude,
      name,
      alternateName,
      longName,
      isInWay
    )
  }

  def toRef: Ref = {
    Ref(
      node.id,
      name
    )
  }
}
