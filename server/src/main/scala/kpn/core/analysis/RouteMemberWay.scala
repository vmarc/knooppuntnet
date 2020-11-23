package kpn.core.analysis

import kpn.api.common.data.Element
import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.server.analyzer.engine.analysis.route.RouteNode
import kpn.server.analyzer.engine.analysis.route.WayAnalyzer

case class RouteMemberWay(
  name: String,
  link: Link,
  role: Option[String],
  way: Way,
  fromNode: Node,
  toNode: Node,
  from: String,
  to:String,
  accessible: Boolean,
  routeNodes: Seq[RouteNode]
) extends RouteMember {

  def memberType: String = "way"

  val endNodes: Seq[Node] = {
    if (way.nodes.isEmpty) {
      Seq()
    }
    else if (WayAnalyzer.isRoundabout(way) || WayAnalyzer.isClosedLoop(way)) {
      way.nodes
    }
    else {
      Seq(way.nodes.head, way.nodes.last)
    }
  }

  def linkName: String = link.name
  def nodes: Seq[RouteNetworkNodeInfo] = routeNodes.map { rn =>
    RouteNetworkNodeInfo(
      rn.id,
      rn.name,
      rn.alternateName,
      rn.node.latitude,
      rn.node.longitude
    )
  }
  def id: Long = way.id
  def element: Element = way
  def linkDescription: String = link.description
  def length: String = way.length.toString + " m"
  def nodeCount: String = way.nodes.size.toString
  def description: String = name
}
