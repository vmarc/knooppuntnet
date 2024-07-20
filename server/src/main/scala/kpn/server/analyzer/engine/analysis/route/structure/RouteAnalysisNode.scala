package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.common.data.Node
import kpn.api.common.route.RouteNode

case class RouteAnalysisNode(
  node: Node,
  name: String,
  alternateName: String,
  //  longName: Option[String] = None,
  //  definedInRelation: Boolean = false,
  //  definedInWay: Boolean = false,
  isInWay: Boolean,
) {
  def toRouteNode: RouteNode = {
    RouteNode(
      node.id,
      node.latitude,
      node.longitude,
      name,
      alternateName,
      isInWay
    )
  }
}
